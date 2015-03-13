package attendance.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import attendance.Loggers;
import attendance.model.AttendanceItem;
import attendance.model.AttendanceItemStatus;
import attendance.model.Employee;
import attendance.repository.AttendanceItemRepository;
import attendance.service.EmployeeService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
	
	private static final Logger LOGGER = Loggers.CONTROLLER_LOGGER;
	
	private final EmployeeService employeeService;
	private final AttendanceItemRepository attendanceItemRepository;
	
	@Autowired
	public ManagerController(
			EmployeeService employeeService,
			AttendanceItemRepository attendanceItemRepository ) {
		
		this.employeeService = employeeService;
		this.attendanceItemRepository = attendanceItemRepository;

	}
	
	@RequestMapping("/listPending")
	public String listPendingApprovals( Principal principal, Model model ) {
    	
    	String gid = principal.getName();
    	Optional<Employee> manager = employeeService.get(gid);
    	
		LOGGER.debug("Called list pending approvals for " + gid + ", retrieved " + manager);
		
    	if( manager.isPresent()) {
			Collection<Employee> subordinates = employeeService.getSubordinates(manager.get());
			
			LOGGER.debug("Retrieved " + subordinates.size() + " subordinates for " + gid);
			
			Collection<AttendanceItem> attendanceItems = new ArrayList<>();
			for(Employee subordinate : subordinates) {
		    	Collection<AttendanceItem> pendingItems =
		    			attendanceItemRepository.findByEmployeeAndAttendanceItemStatus(subordinate, AttendanceItemStatus.APPROVAL_REQUESTED);
				attendanceItems.addAll(pendingItems);
			}
	
	    	model.addAttribute("attendanceItems", attendanceItems);
    	}
    	
        return "/manager/listPending";
	}
}
