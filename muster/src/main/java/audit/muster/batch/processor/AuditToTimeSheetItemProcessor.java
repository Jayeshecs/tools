/**
 * 
 */
package audit.muster.batch.processor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.item.ItemProcessor;

import audit.muster.bean.Audit;
import audit.muster.bean.TimeSheet;
import audit.muster.jpa.repo.TimeSheetRepository;

/**
 * 
 * @author Prajapati
 */
public class AuditToTimeSheetItemProcessor implements ItemProcessor<Audit, TimeSheet> {
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private TimeSheetRepository timeSheetRepository;
	
	private Map<String, TimeSheet> cache = new ConcurrentHashMap<>(1000); 
	
	public AuditToTimeSheetItemProcessor(TimeSheetRepository timeSheetRepository) {
		this.timeSheetRepository = timeSheetRepository;
	}
	
	
	@Override
	public TimeSheet process(final Audit audit) throws Exception {

		TimeSheet timeSheet = null;
		String date = simpleDateFormat.format(audit.getDateEvent());
		List<TimeSheet> timeSheetList = timeSheetRepository.findByDate(date);
		if(timeSheetList != null && !timeSheetList.isEmpty()) {
			timeSheet = timeSheetList.get(0);
			TimeSheet cached = cache.get(date);
			if(cached != null) {
				timeSheet.setCheckIn(new Date(Math.min(cached.getCheckIn().getTime(), timeSheet.getCheckIn().getTime())));
				timeSheet.setCheckOut(new Date(Math.min(cached.getCheckOut().getTime(), timeSheet.getCheckOut().getTime())));
			}
			cache.put(date, timeSheet);
		} else {
			timeSheet = cache.get(date);
			if(timeSheet == null) {
				timeSheet = new TimeSheet(date, audit.getTimeOnly(), audit.getTimeOnly());
				timeSheet = timeSheetRepository.save(timeSheet);
				cache.put(date, timeSheet);
			}
		}
		timeSheet.setCheckIn(new Date(Math.min(timeSheet.getCheckIn().getTime(), audit.getTimeOnly().getTime())));
		timeSheet.setCheckOut(new Date(Math.max(timeSheet.getCheckOut().getTime(), audit.getTimeOnly().getTime())));
		
		return timeSheet;
	}
}
