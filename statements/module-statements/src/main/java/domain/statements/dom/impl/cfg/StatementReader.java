/**
 * 
 */
package domain.statements.dom.impl.cfg;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.DomainObjectLayout.CssClassFaPosition;

import domain.statements.dom.AbstractNamedEntity;

/**
 * @author Prajapati
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "statements")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.VERSION_NUMBER, column="version")
@javax.jdo.annotations.Unique(name="StatementReader_name_UNQ", members = {"name"})
@DomainObject(auditing = Auditing.ENABLED)
@DomainObjectLayout(cssClassFa = "fa-file-alt", cssClassFaPosition = CssClassFaPosition.LEFT)  // causes UI events to be triggered
public class StatementReader extends AbstractNamedEntity<StatementReader> {
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@lombok.Getter @lombok.Setter @lombok.NonNull
	private ReaderType readerType;
    
    @javax.jdo.annotations.Column(allowsNull = "true")
	@lombok.Getter @lombok.Setter
    private Map<String, String> properties;

	/**
	 * @param name
	 * @param clazz
	 */
	public StatementReader(String name, ReaderType readerType) {
		super(name);
		setReaderType(readerType);
	}

	@Override
	protected String getTitlePrefix() {
		return "RDR ";
	}

}
