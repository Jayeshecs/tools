/**
 * 
 */
package statements.application.categorizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.ref.CategoryService;
import domain.statements.dom.srv.ref.SubCategoryService;
import domain.statements.dom.srv.txn.TransactionService;
import lombok.extern.slf4j.Slf4j;
import statements.application.categorizer.tools.Categorizer;
import statements.application.categorizer.tools.Categorizer.ICategorizerDataHandler;
import statements.application.services.categorizer.CategorizationTransaction;
import statements.application.services.categorizer.CategorizationViewModel;

/**
 * @author jayeshecs
 *
 */
@DomainService(
        nature = NatureOfService.DOMAIN,
        objectType = "statements.TransactionCategorizerService",
        repositoryFor = Transaction.class
)
@Slf4j
public class TransactionCategorizerService {
	
	private static final Pattern wordSeparatorPattern = Pattern.compile("[\\.:@$#,_[-]\"\'\\(\\)\\[\\]|/?!;]+");
	
	@Inject
	private TransactionService transactionService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private SubCategoryService subCategoryService;
	
	@Transient
	private ICategorizerDataHandler<Transaction, String> handler = new TransactionCategorizerDataHandler();
	
	public TransactionCategorizerService() {
		// DO NOTHING
	}
	
	@Programmatic
	public CategorizationViewModel categorize() {
		CategorizationViewModel result = categorizeByTransactionType(TransactionType.CREDIT);
		result.getPendingApprovalTransactions().addAll(categorizeByTransactionType(TransactionType.DEBIT).getPendingApprovalTransactions());
		return result;
	}
	
	@Programmatic
	public void train() {
		Categorizer<Transaction, String> categorizer = new Categorizer<>(handler, new NarrationPreProcessor());
		trainByTransactionType(TransactionType.CREDIT, categorizer, true);
		trainByTransactionType(TransactionType.DEBIT, categorizer, true);
	}
	
	@Programmatic
	public CategorizationViewModel categorizeByTransactionType(TransactionType type) {
		// https://github.com/deeplearning4j/dl4j-examples/blob/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/nlp/paragraphvectors/ParagraphVectorsClassifierExample.java
		
		Categorizer<Transaction, String> categorizer = new Categorizer<>(handler, new NarrationPreProcessor());
		
		ParagraphVectors model = trainByTransactionType(type, categorizer, false);
		
		List<Transaction> listUncategorized = transactionService.list(type, null, null, null, true, null, null, null);
		
		Map<Transaction, String> categorized = categorizer.categorize(model, listUncategorized);
		
		CategorizationViewModel result = new CategorizationViewModel();
		serviceRegistery.injectServicesInto(result);
		for (Entry<Transaction, String> entry : categorized.entrySet()) {
			Transaction transaction = entry.getKey();
			Category category = getCategoryFromLabel(entry.getValue());
			SubCategory subCategory = getSubCategoryFromLabel(entry.getValue());
			if (category != null && subCategory != null) {
				log.info(String.format("%s => Category: %s, Sub-category: %s", handler.getParagraph(transaction), category.getName(), subCategory.getName()));
				result.addTransactionForApproval(transaction, category, subCategory);
			}
//			transaction.setCategory(category);
//			transaction.setSubCategory(subCategory);
//			transactionService.save(transaction);
		}
		
		return result;
	}

	private ParagraphVectors trainByTransactionType(TransactionType type, Categorizer<Transaction, String> categorizer, boolean forceTrain) {
		// Get all categorized transactions for training purpose
		List<Transaction> listAllCategorized = transactionService.list(type, null, null, null, false, null, null, null);
		
		File modelFile = new File(type.name() + "transaction_categorizer_model.zip");
		ParagraphVectors model = forceTrain ? null : categorizer.loadModel(modelFile);
		
		if (model == null) {
			model = categorizer.train(listAllCategorized);
			categorizer.save(model, modelFile);
		}
		return model;
	}

	private SubCategory getSubCategoryFromLabel(String value) {
		return subCategoryService.findByNameExact(value.split("_")[1]);
	}

	private Category getCategoryFromLabel(String value) {
		return categoryService.findByNameExact(value.split("_")[0]);
	}
	
	@Inject
	ServiceRegistry serviceRegistery;
		
	/**
	 * Replace common word separator e.g. underscore (_) and dash (-) with whitespace
	 */
	class NarrationPreProcessor extends CommonPreprocessor {
		
		@Override
		public String preProcess(String token) {
			String preProcess = super.preProcess(token);
			preProcess = wordSeparatorPattern.matcher(preProcess).replaceAll(" ");
			String[] words = preProcess.split(" ");
			for(int i = 0; i < words.length; ++i) {
				if (words[i].length() < 3) {
					words[i] = "";
				}
			}
			preProcess = String.join(" ", words);
			return preProcess.trim();
		}
	}
	
	class TransactionCategorizerDataHandler implements ICategorizerDataHandler<Transaction, String> {
		@Override
		public String getLabel(Transaction transaction) {
			if (transaction == null || transaction.getCategory() == null) {
				// This should never happen !
				return null;
			}
			String category = transaction.getCategory().getName();
			String subCategory = "";
			if (transaction.getSubCategory() != null) {
				subCategory = transaction.getSubCategory().getName();
			}
			String label = String.format("%s_%s", category, subCategory);
			return label;
		}
		
		@Override
		public String toLabel(String key) {
			return key;
		}

		@Override
		public String getParagraph(Transaction data) {
			return data.getNarration(); //new StringBuilder(data.getType().name()).append(' ').append(data.getNarration()).toString();
		}
	}
	
}
