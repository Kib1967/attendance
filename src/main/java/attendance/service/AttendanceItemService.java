package attendance.service;

import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Component;

import attendance.Loggers;
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
		
		if(employee.isPresent()) {
			
			if(attendanceItem.getAttendanceItemType().isNeedsApproval()) {
				attendanceItem.setAttendanceItemStatus(AttendanceItemStatus.APPROVAL_REQUESTED);
			}
			else {
				attendanceItem.setAttendanceItemStatus(AttendanceItemStatus.CREATED);
			}
			attendanceItem.setEmployee(employee.get());
		
			attendanceItemRepository.save(attendanceItem);
			
			if(attendanceItem.getAttendanceItemType().isNeedsApproval()) {
				
				Employee manager = attendanceItem.getEmployee().getManager();
				if(manager != null) {
					try {
						LOGGER.info("Sending email");
						// TODO Get Thymeleaf template and populate
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
		}
		else {
			throw new EmployeeNotFoundException( gid );
		}
	}
}