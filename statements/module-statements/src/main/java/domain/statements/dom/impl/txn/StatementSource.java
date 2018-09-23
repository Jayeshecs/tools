/**
 * 
 */
package domain.statements.dom.impl.txn;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;

import domain.statements.dom.AbstractNamedEntity;

/**
 * @author Prajapati
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "statements")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.VERSION_NUMBER, column="version")
@javax.jdo.annotations.Unique(name="StatementSource_name_UNQ", members = {"name"})
@DomainObject(auditing = Auditing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()  // causes UI events to be triggered
public class StatementSource extends AbstractNamedEntity<StatementSource> {
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@Extension(vendorName="datanucleus", key="enum-value-getter", value="id")
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private StatementSourceType type;

	/**
	 * @param name
	 */
	public StatementSource(String name, StatementSourceType type) {
		super(name);
		setType(type);
	}

	@Override
	protected String getTitlePrefix() {
		return "SRC ";
	}


}
