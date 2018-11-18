/**
 * 
 */
package domain.statements.dom.impl.cfg;

import javax.jdo.AttributeConverter;

/**
 * @author Prajapati
 *
 */
public class ClassAttributeConverter implements AttributeConverter<Class<?>, String> {

	/* (non-Javadoc)
	 * @see javax.jdo.AttributeConverter#convertToAttribute(java.lang.Object)
	 */
	@Override
	public Class<?> convertToAttribute(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.jdo.AttributeConverter#convertToDatastore(java.lang.Object)
	 */
	@Override
	public String convertToDatastore(Class<?> clazz) {
		return clazz.getName();
	}

}
