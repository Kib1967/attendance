package attendance.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import attendance.model.Employee;
import attendance.repository.EmployeeRepository;

@Component
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public Optional<Employee> get( String gid ) {
		return employeeRepository.findByGid(gid);
	}
}
