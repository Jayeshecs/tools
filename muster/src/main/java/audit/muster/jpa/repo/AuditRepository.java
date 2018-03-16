/**
 * 
 */
package audit.muster.jpa.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import audit.muster.bean.Audit;

/**
 * @author Prajapati
 *
 */
public interface AuditRepository extends CrudRepository<Audit, Long> {

	/**
	 * @param source
	 * @return
	 */
	List<Audit> findBySourceAndTaskCategoryAndDateEvent(String source, String taskCategory, Date dateEvent);

	/**
	 * @return
	 */
	Audit findTopByOrderByDateEventDesc();

	/**
	 * @return
	 */
	Audit findTopByOrderByDateEventAsc();
}
