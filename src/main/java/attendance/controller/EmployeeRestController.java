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
import attendance.model.Employee;
import attendance.repository.EmployeeRepository;

@RestController
@RequestMapping("/employee")
class EmployeeRestController {

	private final EmployeeRepository employeeRepository;

	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> add(@PathVariable String gid, @RequestBody Employee employee) {
		return this.employeeRepository
				.findByGid(gid)
				.map(account -> {
					Employee result = employeeRepository.save(new Employee(employee.getGid(), employee.getName(), null, null));

					HttpHeaders httpHeaders = new HttpHeaders();
					httpHeaders.setLocation(ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getId()).toUri());
					return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
				}).get();

	}

	@RequestMapping(value = "/{gid}", method = RequestMethod.GET)
	Employee readEmployee(@PathVariable String gid) throws EmployeeNotFoundException {
		return this.employeeRepository.findByGid(gid).orElseThrow(() -> new EmployeeNotFoundException(gid));
	}

	@RequestMapping(method = RequestMethod.GET)
	Collection<Employee> readEmployees() {
		return this.employeeRepository.findAll();
	}

	@Autowired
	EmployeeRestController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
}
