package attendance.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import attendance.EmployeeNotFoundException;
import attendance.model.AttendanceItem;
import attendance.model.Employee;
import attendance.repository.AttendanceItemRepository;
import attendance.repository.EmployeeRepository;

@RestController
@RequestMapping("/{employeeId}/attendanceItem")
class AttendanceItemRestController {

	private final EmployeeRepository employeeRepository;

	private final AttendanceItemRepository attendanceItemRepository;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@PathVariable String employeeId, @RequestBody AttendanceItem input) {
		return this.employeeRepository
				.findByGid(employeeId)
				.map(employee -> {
					AttendanceItem result = attendanceItemRepository.save(new AttendanceItem(employee,
							input.getAttendanceItemStatus(), input.getAttendanceItemType(),
							input.getStartDate(), input.getEndDate()));

					HttpHeaders httpHeaders = new HttpHeaders();
					httpHeaders.setLocation(ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getId()).toUri());
					return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
				}).get();

	}

	@RequestMapping(value = "/{attendanceItemId}", method = RequestMethod.GET)
	AttendanceItem readAttendanceItem(@PathVariable String id, @PathVariable Long attendanceItemId) throws EmployeeNotFoundException {
		this.validateEmployee(id);
		return this.attendanceItemRepository.findOne(attendanceItemId);
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<AttendanceItem> readAttendanceItems(@PathVariable String gid) throws EmployeeNotFoundException {
		Employee employee = this.validateEmployee(gid);
		return this.attendanceItemRepository.findByEmployee(employee);
	}

	@Autowired
	AttendanceItemRestController(EmployeeRepository employeeRepository,
			AttendanceItemRepository attendanceItemRepository) {
		this.employeeRepository = employeeRepository;
		this.attendanceItemRepository = attendanceItemRepository;
	}

	private Employee validateEmployee(String gid) throws EmployeeNotFoundException {
		return this.employeeRepository.findByGid(gid).orElseThrow(
				() -> new EmployeeNotFoundException(gid));
	}
}