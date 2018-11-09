/**
 * 
 */
package domain.statements.dom.impl.ref;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;

import domain.statements.dom.AbstractNamedEntity;
import domain.statements.dom.types.Notes;

/**
 * @author Prajapati
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "statements")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.VERSION_NUMBER, column="version")
@javax.jdo.annotations.Unique(name="SubCategory_name_UNQ", members = {"name"})
@DomainObject(auditing = Auditing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()  // causes UI events to be triggered
public class SubCategory extends AbstractNamedEntity<SubCategory> {

	@lombok.Getter @lombok.Setter
    @Notes private String notes;
	
	/**
	 * @param name
	 */
	public SubCategory(String name) {
		super(name);
	}

	@Override
	protected String getTitlePrefix() {
		return "";
	}


}
