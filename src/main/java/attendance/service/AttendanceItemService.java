package attendance.service;

import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Component;

import attendance.ApprovalRequestNotSentException;
import attendance.EmployeeNotFoundException;
import attendance.Loggers;
import attendance.ManagerNotFoundException;
import attendance.model.AttendanceItem;
import attendance.model.AttendanceItemStatus;
import attendance.model.Employee;
import attendance.repository.AttendanceItemRepository;

@Component
public class AttendanceItemService {
	
	private static final Logger LOGGER = Loggers.SERVICE_LOGGER;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AttendanceItemRepository attendanceItemRepository;

	public void create(
			String gid,
			AttendanceItem attendanceItem) throws EmployeeNotFoundException, ManagerNotFoundException, ApprovalRequestNotSentException {
		Optional<Employee> employee = employeeService.get(gid);
		
		attendanceItem.setAttendanceItemStatus( AttendanceItemStatus.REQUESTED );
		if(employee.isPresent()) {
			attendanceItem.setEmployee(employee.get());
		
			attendanceItemRepository.save(attendanceItem);
			
			Employee manager = attendanceItem.getEmployee().getManager();
			if(manager != null) {
				try {
					LOGGER.warn("Sending email");
					emailService.send(manager, "Testing", "Testing");
				}
				catch(MessagingException e) {
					LOGGER.error("Exception sending email", e);
					throw new ApprovalRequestNotSentException(e);
				}
				catch(MailAuthenticationException e) {
					LOGGER.error("Exception sending email", e);
					throw new ApprovalRequestNotSentException(e);
				}
			}
			else {
				throw new ManagerNotFoundException( gid );
			}
		}
		else {
			throw new EmployeeNotFoundException( gid );
		}
	}
}