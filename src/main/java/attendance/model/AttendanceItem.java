package attendance.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Access(value=AccessType.FIELD)
public class AttendanceItem {
	
    @JsonIgnore
    @ManyToOne
    private Employee employee;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
	private AttendanceItemType attendanceItemType;
	private AttendanceItemStatus attendanceItemStatus;
	private Date startDate;
	private Date endDate;
	
	public AttendanceItem(Employee employee, AttendanceItemStatus attendanceItemStatus,
			AttendanceItemType attendanceItemType, Date startDate,
			Date endDate) {
		this.employee = employee;
		this.attendanceItemType = attendanceItemType;
		this.attendanceItemStatus = attendanceItemStatus;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public AttendanceItem() {
	}
	
    public Long getId() {
        return id;
    }

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public AttendanceItemType getAttendanceItemType() {
		return attendanceItemType;
	}

	public void setAttendanceItemType(AttendanceItemType attendanceItemType) {
		this.attendanceItemType = attendanceItemType;
	}

	public AttendanceItemStatus getAttendanceItemStatus() {
		return attendanceItemStatus;
	}

	public void setAttendanceItemStatus(AttendanceItemStatus attendanceItemStatus) {
		this.attendanceItemStatus = attendanceItemStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
