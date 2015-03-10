package attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import attendance.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByGid(String gid);
}