/**
 * 
 */
package domain.statements.dom.impl.txn;

import java.math.BigDecimal;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;

import domain.statements.dom.AbstractEntity;
import domain.statements.dom.types.Notes;

/**
 * @author Prajapati
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "statements")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.VERSION_NUMBER, column="version")
@javax.jdo.annotations.Unique(name="Transaction_hash_UNQ", members = {"type", "transactionDate", "narration", "reference"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout()  // causes UI events to be triggered
public class Transaction extends AbstractEntity<Transaction> {
	
	@javax.jdo.annotations.PrimaryKey
    @javax.jdo.annotations.Column(name="id", allowsNull="false")
	@Property(editing = Editing.DISABLED)
    @Title(prepend = "TXN ")
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private Long id;
	
    @javax.jdo.annotations.Column(allowsNull = "false")
    @Property(editing = Editing.ENABLED)
	@Extension(vendorName="datanucleus", key="enum-value-getter", value="id")
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private TransactionType type;
	
    @javax.jdo.annotations.Column(name="SOURCE_ID", allowsNull = "false")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private StatementSource source;
	
    @javax.jdo.annotations.Column(name="CATEGORY_ID")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter
	private Category category;
	
    @javax.jdo.annotations.Column(name="SUB_CATEGORY_ID")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter
	private SubCategory subCategory;
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private BigDecimal amount;
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private Date transactionDate;
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private Date valueDate;
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter @lombok.NonNull
	@Notes private String narration;
	
    @javax.jdo.annotations.Column(allowsNull = "true")
	@Property(editing = Editing.ENABLED)
	@lombok.Getter @lombok.Setter
	private String reference;
}
