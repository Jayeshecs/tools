/**
 * 
 */
package audit.muster.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Prajapati
 *
 */
@Entity
public class Audit {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Getter @Setter
    private Long id;
	
	@Getter @Setter
	private String keyword;
	
	@Getter @Setter
	private Date dateEvent;
	
	@Getter @Setter
	private Date dateOnly;
	
	@Getter @Setter
	private Date timeOnly;
	
	@Getter @Setter
	private String source;
	
	@Getter @Setter
	private String eventId;
	
	@Getter @Setter
	private String taskCategory;
	
	@Getter @Setter
	@Column(columnDefinition = "clob")
	@Lob
	private String comment;
	
	/**
	 * 
	 */
	public Audit() {
		// DO NOTHING
	}
	
	/**
	 * @param keyword
	 * @param dateEvent
	 * @param source
	 * @param eventId
	 * @param taskCategory
	 */
	public Audit(String keyword, Date dateEvent, String source, String eventId, String taskCategory, String comment) {
		this.keyword = keyword;
		this.dateEvent = dateEvent;
		this.source = source;
		this.eventId = eventId;
		this.taskCategory = taskCategory;
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Audit [keyword=%s, dateEvent=%s, source=%s, eventId=%s, taskCategory=%s]", keyword, dateEvent, source, eventId, taskCategory);
	}

}
