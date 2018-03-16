/**
 * 
 */
package audit.muster.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Prajapati
 *
 */
@Entity
public class TimeSheet {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Getter @Setter
    private Long id;
	
	@Getter @Setter
	private String date;
	
	@Getter @Setter
	private Date checkIn;
	
	@Getter @Setter
	private Date checkOut;

	public TimeSheet() {
		// DO NOTHING
	}
	
	public TimeSheet(String date, Date checkIn, Date checkOut) {
		this.date = date;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("TimeSheet [id=%s, date=%s, checkIn=%s, checkOut=%s]", id, date, checkIn, checkOut);
	}
}
