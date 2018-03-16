/**
 * 
 */
package audit.muster.jpa.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import audit.muster.bean.TimeSheet;

/**
 * @author Prajapati
 *
 */
public interface TimeSheetRepository extends CrudRepository<TimeSheet, Long> {

	List<TimeSheet> findAllByOrderByDateAsc();
	
	/**
	 * @param source
	 * @return
	 */
	List<TimeSheet> findByDate(String date);

	/**
	 * @return
	 */
	TimeSheet findTopByOrderByDateDesc();

	/**
	 * @return
	 */
	TimeSheet findTopByOrderByDateAsc();
}
