/**
 * 
 */
package audit.muster.batch.listener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.hsqldb.util.CSVWriter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import audit.muster.bean.Audit;
import audit.muster.bean.TimeSheet;
import audit.muster.jpa.repo.AuditRepository;
import audit.muster.jpa.repo.TimeSheetRepository;

/**
 * @author Prajapati
 *
 */
@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final long SECONDS = 1000;
	
	private static final long MINUTES = 60 * SECONDS;

	private static final long HOURS = 60 * MINUTES;
	
	private final AuditRepository auditRepository;
	private final TimeSheetRepository timeSheetRepository;

	@Autowired
	public JobCompletionNotificationListener(AuditRepository auditRepository, TimeSheetRepository timeSheetRepository) {
		this.auditRepository = auditRepository;
		this.timeSheetRepository = timeSheetRepository;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
	    	log.info("Batch processing completed");
			Iterable<Audit> audits = auditRepository.findAll();
			
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				SimpleDateFormat simpleDateOnlyFormat = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat simpleTimeOnlyFormat = new SimpleDateFormat("HH:mm");
				File file = new File("./out/audit-output.csv");
				file.getParentFile().mkdirs();
				CSVWriter writer = new CSVWriter(file, "UTF-8");
				writer.writeHeader(new String[] { "Keyword", "Date time","Source", "Event ID", "Task Category", "Date only", "Time only" });
				for (Audit audit : audits) {
					writer.writeData(new String[]{audit.getKeyword(), simpleDateFormat.format(audit.getDateEvent()), audit.getSource(), audit.getEventId(), audit.getTaskCategory(), simpleDateOnlyFormat.format(audit.getDateOnly()), simpleTimeOnlyFormat.format(audit.getTimeOnly())});
				}
				writer.close();
				log.info("Audit output file is successfully written - " + file);
				Iterable<TimeSheet> timeSheetList = timeSheetRepository.findAllByOrderByDateAsc();
				Audit latestAudit = auditRepository.findTopByOrderByDateEventDesc();
				Audit oldestAudit = auditRepository.findTopByOrderByDateEventAsc();
				SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
				Date endDate = latestAudit.getDateOnly();
				Date startDate = oldestAudit.getDateOnly();
				Map<String, String[]> dateLedger = new LinkedHashMap<String, String[]>();
				while(startDate.getTime() <= endDate.getTime()) {
					String date = sdfYYYYMMDD.format(startDate);
					dateLedger.put(date, new String[]{date, "", "", "00:00"});
					startDate = new Date(startDate.getTime() + (24 * HOURS));
				}
				log.info(String.format("Ledger[ Start: %s, End: %s, Total: %d, Expected: %d]", oldestAudit.getDateOnly(), endDate, dateLedger.size(), ((endDate.getTime() - oldestAudit.getDateOnly().getTime())/ (24 * HOURS))));
				
				for (TimeSheet timeSheet : timeSheetList) {
					Date checkInTime = new Date(timeSheet.getCheckIn().getTime() - (15 * MINUTES));
					Date checkOutTime = new Date(timeSheet.getCheckOut().getTime() + (15 * MINUTES));
					long hours = (checkOutTime.getTime() - checkInTime.getTime()) / HOURS;
					long minutes = ((checkOutTime.getTime() - checkInTime.getTime()) / MINUTES) - (hours * 60);
					dateLedger.put(timeSheet.getDate(), new String[]{
							timeSheet.getDate(), 
							simpleTimeOnlyFormat.format(checkInTime), 
							simpleTimeOnlyFormat.format(checkOutTime),
							String.format("%d:%d", hours, minutes )
					});
				}
				file = new File("./out/timesheet-output.csv");
				CSVWriter timeSheetWritter = new CSVWriter(file, "UTF-8");
				timeSheetWritter.writeHeader(new String[] { "Date", "Check-in","Check-out", "Duration (hrs)"});
				for (Entry<String, String[]> entry : dateLedger.entrySet()) {
					timeSheetWritter.writeData(entry.getValue());
				}
				timeSheetWritter.close();
				log.info("Timesheet output file is successfully written - " + file);
			} catch (IOException e) {
				log.error("I/O error occurred while writting audit to output file", e);
			}

		} else if(jobExecution.getStatus() == BatchStatus.STARTED) {
			log.info("Batch processing started");
		}
	}
}
