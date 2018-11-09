/**
 * 
 */
package domain.statements.dom.impl.cfg;

import javax.jdo.annotations.Discriminator;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Publishing;

import domain.statements.dom.types.Name;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Prajapati
 *
 */
@DomainObject(
	publishing = Publishing.DISABLED, 
	auditing = Auditing.DISABLED, 
	bounding = Bounding.BOUNDED, 
	nature = Nature.INMEMORY_ENTITY, 
	objectType = "statements.ReaderType"
)
@DomainObjectLayout()  // causes UI events to be triggered
@Discriminator("statements.ReaderType")
public class ReaderType {

	@Name @Property
	@PropertyLayout
	@Getter @Setter
	private String name;
	
	@Property
	@PropertyLayout
	@Getter @Setter
	private Class<?> clazz;

	/**
	 * @param name
	 */
	public ReaderType(String name, Class<?> clazz) {
		setName(name);
		setClazz(clazz);
	}

}
