package kz.dulaty.queue.feature.department.data.repository;

import kz.dulaty.queue.feature.department.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
