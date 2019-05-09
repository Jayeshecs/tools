/**
 * 
 */
package statements.application.services.categorizer;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.ActionLayout.Position;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.registry.ServiceRegistry;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.dom.impl.txn.Transaction;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jayeshecs
 *
 */
@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "statements.application.services.categorizer.CategorizationViewModel"
)
@Slf4j
public class CategorizationViewModel {
	
	@org.apache.isis.applib.annotation.Collection
	private Collection<CategorizationTransaction> pendingApprovalTransactions = new ArrayList<CategorizationTransaction>();
	
	public CategorizationViewModel() {
		// DO NOTHING
	}

    public TranslatableString title() {
        return TranslatableString.tr("Pending approval transactions");
    }

    public Collection<CategorizationTransaction> getPendingApprovalTransactions() {
        return pendingApprovalTransactions;
    }
    
    public void setPendingApprovalTransactions(Collection<CategorizationTransaction> pendingApprovalTransactions) {
		this.pendingApprovalTransactions = pendingApprovalTransactions;
	}
    
    @Programmatic
    public void addTransactionForApproval(Transaction transaction, Category category, SubCategory subCategory) {
    	CategorizationTransaction proposedTransaction = new CategorizationTransaction();
    	serviceRegistery.injectServicesInto(proposedTransaction);
    	proposedTransaction.fromTransaction(transaction);
    	proposedTransaction.setCategory(category);
    	proposedTransaction.setSubCategory(subCategory);
    	pendingApprovalTransactions.add(proposedTransaction);
    }

    @Action(semantics = SemanticsOf.SAFE, associateWith = "pendingApprovalTransactions", associateWithSequence = "1")
    @ActionLayout(named = "Approve Selected Categorizations", position = Position.PANEL)
    public void approve(Collection<CategorizationTransaction> transactions) {
    	// TODO
    	log.info("Total categorizations approved are -> " + transactions.size());
    }
    
    @Inject
    ServiceRegistry serviceRegistery;

}
