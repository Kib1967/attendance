package attendance.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Access(value=AccessType.FIELD)
public class Employee {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String gid;
	private String name;
	private String emailAddress;
	
    @ManyToOne
	private Employee manager;
	
    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();
    
    @OneToMany(mappedBy = "employee")
    private Set<AttendanceItem> attendanceItems = new HashSet<>();
    
    public Employee( String gid, String name, String emailAddress, Employee manager ) {
    	this.gid = gid;
    	this.name = name;
    	this.emailAddress = emailAddress;
    	this.manager = manager;
    }
    
    Employee() { // for JPA
    }

	public Long getId() {
		return id;
	}

	public String getGid() {
		return gid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public Set<AttendanceItem> getAttendanceItems() {
		return attendanceItems;
	}

	public Employee getManager() {
		return manager;
	}

	public Set<Employee> getSubordinates() {
		return subordinates;
	}
}
