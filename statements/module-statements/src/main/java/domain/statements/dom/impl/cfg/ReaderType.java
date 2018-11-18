/**
 * 
 */
package domain.statements.dom.impl.cfg;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;

import domain.statements.dom.AbstractNamedEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Prajapati
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "statements")
@javax.jdo.annotations.DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.VERSION_NUMBER, column="version")
@javax.jdo.annotations.Unique(name="ReaderType_name_UNQ", members = {"name"})
@DomainObject(
	publishing = Publishing.ENABLED, 
	auditing = Auditing.ENABLED, 
	bounding = Bounding.BOUNDED,
	objectType = "statements.ReaderType"
)
@DomainObjectLayout(plural = "Reader types")  // causes UI events to be triggered
public class ReaderType extends AbstractNamedEntity<ReaderType> {
	
	@Property(optionality = Optionality.OPTIONAL)
	@PropertyLayout(named = "Reader Class")
	@Getter @Setter
	@Column(allowsNull = "true")
	@MemberOrder(sequence = "2")
	private String className;
	
	/**
	 * Vanilla constructor
	 */
	public ReaderType() {
		super("dummy");
		// support framework
	}

	/**
	 * @param name
	 */
	public ReaderType(String name, String className) {
		super(name);
		setClassName(className);
	}

	@Override
	protected String getTitlePrefix() {
		return "";
	}

}
