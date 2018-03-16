/**
 * 
 */
package audit.muster.batch.processor;

import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;

import audit.muster.bean.Audit;
import audit.muster.jpa.repo.AuditRepository;

/**
 * 
 * @author Prajapati
 */
@Slf4j
public class AuditItemProcessor implements ItemProcessor<Audit, Audit> {

	@SuppressWarnings("unused")
	private AuditRepository auditRepository;

	public AuditItemProcessor(AuditRepository auditRepository) {
		this.auditRepository = auditRepository;
	}
	
	
	@Override
	public Audit process(final Audit audit) throws Exception {
		Audit transformedAudit = audit;
//		List<Audit> audit2 = null;//auditRepository.findBySourceAndTaskCategoryAndDateEvent(audit.getSource(), audit.getTaskCategory(), audit.getDateEvent());
//		if(audit2 != null && !audit2.isEmpty()) {
//			transformedAudit = audit2.get(0);
//		}
		if(transformedAudit.getDateOnly() == null) {
			transformedAudit.setDateOnly(trunc(transformedAudit.getDateEvent()));
		}
		if(transformedAudit.getTimeOnly() == null) {
			transformedAudit.setTimeOnly(transformedAudit.getDateEvent());
		}
		log.trace("Converting (" + audit + ") into (" + transformedAudit + ")");

		return transformedAudit;
	}


	private Date trunc(Date dateEvent) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateEvent);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);

		Date date = calendar.getTime();
		log.trace("trunc (" + dateEvent + ") into (" + date + ")");
		if((dateEvent.getTime() - date.getTime()) >= 20 * 60 * 60 * 1000) {
			log.error("Trunc error: " + dateEvent + ", " + date);
		}
		return date;
	}
}
