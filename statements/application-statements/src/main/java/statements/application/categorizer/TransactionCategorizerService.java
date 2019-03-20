/**
 * 
 */
package statements.application.categorizer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.text.documentiterator.LabelAwareDocumentIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

import domain.statements.dom.impl.txn.Transaction;
import domain.statements.dom.srv.txn.TransactionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import statements.application.categorizer.tools.Categorizer;
import statements.application.categorizer.tools.Categorizer.ICategorizerDataHandler;
import statements.application.categorizer.tools.LabelSeeker;
import statements.application.categorizer.tools.MeansBuilder;

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
	
	@Inject
	private TransactionService transactionService;
	private DefaultTokenizerFactory tokenizerFactory;
	
	public TransactionCategorizerService() {
		// DO NOTHING
	}
	
	@Programmatic
	public void categorize() {
		// https://github.com/deeplearning4j/dl4j-examples/blob/master/dl4j-examples/src/main/java/org/deeplearning4j/examples/nlp/paragraphvectors/ParagraphVectorsClassifierExample.java
		
		// Get all categorized transactions for training purpose
		List<Transaction> listAllCategorized = transactionService.list(null, null, null, false, null, null, null);
		
		ICategorizerDataHandler<Transaction, String> handler = new ICategorizerDataHandler<Transaction, String>() {

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
			public String getParagraph(Transaction data) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		Categorizer<Transaction, String> categorizer = new Categorizer(handler, new NarrationPreProcessor());

		// initialize tokenizer factory
		tokenizerFactory = new DefaultTokenizerFactory();
		tokenizerFactory.setTokenPreProcessor(new NarrationPreProcessor());

		ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
	              .learningRate(0.025)
	              .minLearningRate(0.001)
	              .batchSize(100)
	              .epochs(1)
	              .iterations(5)
	              //.layerSize(100)
	              .iterate(new CategoryAwareNarrationIterator(listAllCategorized))
	              .trainWordVectors(true)
	              //.minWordFrequency(2)
	              .tokenizerFactory(tokenizerFactory)
	              .build();
		
		// start model training
		paragraphVectors.fit();
		
		VocabCache<VocabWord> vocab = paragraphVectors.getVocab();
		log.info(String.format("Vocab Detail:\n"
				+ "Total words: %d\n"
				+ "Total Docs: %d\n"
				+ "Total Word Occurances: %d\n"
				+ "Total Tokens: %d\n"
				+ "Words: %s\n"
				, vocab.numWords()
				, vocab.totalNumberOfDocs()
				, vocab.totalWordOccurrences()
				, vocab.tokens().size()
				, vocab.words()));
		for (VocabWord vocabWord : vocab.vocabWords()) {
			log.info(String.format("Is Label: %s, Word: %s (%s)"
					, vocabWord.isLabel()
					, vocabWord.getLabel()
					, vocabWord));
		}
		
		List<Transaction> listUncategorized = transactionService.list(null, null, null, true, null, null, null);

		MeansBuilder meansBuilder = new MeansBuilder(
				(InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
				tokenizerFactory);
		
		List<String> labels = paragraphVectors.getLabelsSource().getLabels();
		
		LabelSeeker seeker = new LabelSeeker(new ArrayList<String>(labels),
				(InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());
		
		long count = 0;
		for (Transaction transaction : listUncategorized) {
			INDArray vector = meansBuilder.documentAsVector(transaction.getNarration());
			Pair<String, Double> scores = seeker.getScores(vector);
			if (scores == null) {
				continue;
			}
			if (scores.getValue().isNaN()) {
				continue;
			}
			count++;
			log.info(String.format("%s = %s (%f)", transaction.getNarration(), scores.getKey(), scores.getValue()));
		}
		log.info(String.format("Total %d/%d transactions categorized", count, listUncategorized.size()));
	}
}
