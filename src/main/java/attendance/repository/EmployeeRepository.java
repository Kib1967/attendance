package attendance.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import attendance.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByGid(String gid);
    
    Collection<Employee> findByManager(Employee manager);
}