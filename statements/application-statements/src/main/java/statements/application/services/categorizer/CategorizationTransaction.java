/**
 * 
 */
package statements.application.services.categorizer;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModelLayout;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.impl.ref.SubCategory;
import domain.statements.dom.impl.ref.TransactionType;
import domain.statements.dom.impl.txn.Transaction;

/**
 * @author jayeshecs
 *
 */
@DomainObject(nature = Nature.VIEW_MODEL, objectType = "statements.application.services.categorizer.CategorizationTransaction")
@ViewModelLayout(describedAs = "Categorization Transaction for approval", named = "Categorization Transaction", paged = 100)
public class CategorizationTransaction {

	@Property(editing = Editing.ENABLED)
	@MemberOrder(sequence = "1")
	@lombok.Getter @lombok.Setter
	private BigDecimal amount;
	
	@Property(editing = Editing.ENABLED)
	@MemberOrder(sequence = "2")
	@lombok.Getter @lombok.Setter
	private Date valueDate;
	
	@Property(editing = Editing.ENABLED)
	@MemberOrder(sequence = "3")
	@lombok.Getter @lombok.Setter
	private TransactionType type;
	
	@Property(editing = Editing.ENABLED)
	@MemberOrder(sequence = "4")
	@lombok.Getter @lombok.Setter
	private Category category;
	
	@Property(editing = Editing.ENABLED)
	@MemberOrder(sequence = "5")
	@lombok.Getter @lombok.Setter
	private SubCategory subCategory;
	
	@Property(editing = Editing.ENABLED)
	@MemberOrder(sequence = "6")
	@lombok.Getter @lombok.Setter
	private String narration;
	
	public CategorizationTransaction() {
		// DO NOTHING
	}
	
	@Programmatic
	public void fromTransaction(Transaction transaction) {
		setAmount(transaction.getAmount());
		setNarration(transaction.getNarration());
		setValueDate(transaction.getValueDate());
		setCategory(transaction.getCategory());
		setSubCategory(transaction.getSubCategory());
	}

}
