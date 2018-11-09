/**
 * 
 */
package statements.application.api;

import java.util.Collection;

/**
 * Specification for reader callback
 * 
 * @author Prajapati
 */
public interface IReaderCallback<T> {

	/**
	 * Process given records
	 * 
	 * @param records
	 */
	void process(Collection<T> records);
}
