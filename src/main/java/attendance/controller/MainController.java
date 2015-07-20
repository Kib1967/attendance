package attendance.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import attendance.model.AttendanceItem;
import attendance.model.Employee;
import attendance.repository.AttendanceItemRepository;
import attendance.service.EmployeeService;

@Controller
@RequestMapping("/")
public class MainController {
	
	private final EmployeeService employeeService;
	private final AttendanceItemRepository attendanceItemRepository;
	
	@Autowired
	MainController(
			EmployeeService employeeService,
			AttendanceItemRepository attendanceItemRepository) {
		
		this.employeeService = employeeService;
		this.attendanceItemRepository = attendanceItemRepository;
	}

	
    @RequestMapping("/")
    public String show(Model model, Principal principal) throws IOException {
    	
    	String gid = principal.getName();
		Employee employee = employeeService.get(gid);
		
		Collection<AttendanceItem> attendanceItems;
		if(employee != null) {
	    	attendanceItems = attendanceItemRepository.findByEmployee(employee);
		}
		else {
			attendanceItems = Collections.<AttendanceItem>emptySet();
		}

    	model.addAttribute("attendanceItems", attendanceItems);
    	
        return "index";
    }
    
    
    @RequestMapping("/login")
    public String login() {
    	return "login";
    }
}
