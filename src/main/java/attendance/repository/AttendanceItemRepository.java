package attendance.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import attendance.model.AttendanceItem;
import attendance.model.Employee;

public interface AttendanceItemRepository extends JpaRepository<AttendanceItem, Long> {
    Collection<AttendanceItem> findByEmployee(Employee employee);
}