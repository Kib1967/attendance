package attendance.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import attendance.ApprovalRequestNotSentException;
import attendance.EmployeeNotFoundException;
import attendance.ManagerNotFoundException;
import attendance.StandardModelKeys;
import attendance.model.AttendanceItem;
import attendance.model.AttendanceItemType;
import attendance.repository.AttendanceItemRepository;
import attendance.service.AttendanceItemService;
import attendance.service.EmployeeService;

@Controller
@RequestMapping("/attendanceItem")
public class AttendanceItemController {
	
	@Autowired
	private AttendanceItemRepository attendanceItemRepository;
	
	@Autowired
	private AttendanceItemValidator validator;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AttendanceItemService attendanceItemService;
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createForm(Model model) {
		
		AttendanceItem item = new AttendanceItem();
		item.setAttendanceItemType(AttendanceItemType.ANNUAL_LEAVE);
		
		model.addAttribute("attendanceItem", item);
		
		return "create";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createSubmit(
			@Valid AttendanceItem attendanceItem,
			BindingResult bindingResult,
			Model model,
			Principal principal) {

		String viewName;
		
		if( bindingResult.hasErrors()) {
			
			viewName = "create";
		}
		else {
			String gid = principal.getName();
			
			try {
				attendanceItemService.create( gid, attendanceItem );
				
				model.addAttribute(
						StandardModelKeys.USER_MESSAGE,
						"Successfully created item and sent to manager for approval");
			}
			catch( EmployeeNotFoundException e ) {
				model.addAttribute(
						StandardModelKeys.USER_ERROR_MESSAGE,
						"Unable to locate logged-in user " + gid );
			}
			catch( ManagerNotFoundException e ) {
				model.addAttribute(
						StandardModelKeys.USER_ERROR_MESSAGE,
						"Unable to locate logged-in user " + gid );
			}
			catch( ApprovalRequestNotSentException e ) {
				model.addAttribute(
						StandardModelKeys.USER_WARNING_MESSAGE,
						"Your item was saved, but sending an approval request to your manager failed" );
			}
			
			// Return to list of items
			// Not comfortable doing it like this - a redirect seems more logical and
			// would eliminate the repeated model manipulation code seen here
	    	List<AttendanceItem> attendanceItems = attendanceItemRepository.findAll();
	    	model.addAttribute("attendanceItems", attendanceItems);
	    	
	    	viewName = "index";
		}
		
		return viewName;
	}
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	    
	    binder.registerCustomEditor(Date.class, editor);
	    
        binder.setDisallowedFields("id");
        binder.setValidator(validator);
	}
}
