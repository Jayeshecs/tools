/**
 * 
 */
package domain.statements.dom.impl.cfg;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

import domain.statements.dom.AbstractNamedEntity;

/**
 * @author Prajapati
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "statements")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.VERSION_NUMBER, column="version")
@javax.jdo.annotations.Unique(name="StatementReader_name_UNQ", members = {"name"})
@DomainObject(auditing = Auditing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()  // causes UI events to be triggered
public class StatementReader extends AbstractNamedEntity<StatementReader> {
	
    @javax.jdo.annotations.Column(allowsNull = "false")
	@lombok.Getter @lombok.Setter @lombok.NonNull
    @Property(editing = Editing.ENABLED, optionality = Optionality.MANDATORY)
    @PropertyLayout()
    @MemberOrder(sequence = "2")
	private ReaderType readerType;
    
    @javax.jdo.annotations.Column(allowsNull = "true", length = 2000)
	@lombok.Getter @lombok.Setter
	@Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(labelPosition = LabelPosition.TOP, multiLine = 3, describedAs = "Standard property file format to specify properties to be used by this statement reader")
    @MemberOrder(sequence = "3")
    private String properties;
    
    public StatementReader() {
    	super("dummy");
    	// support framework
    }

	/**
	 * @param name
	 * @param clazz
	 */
	public StatementReader(String name, ReaderType readerType) {
		super(name);
		setReaderType(readerType);
		setProperties("#dateFormat=dd/MM/yy");
	}

	@Override
	protected String getTitlePrefix() {
		return "RDR ";
	}

}
