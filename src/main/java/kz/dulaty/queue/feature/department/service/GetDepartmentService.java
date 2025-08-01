package kz.dulaty.queue.feature.department.service;

import kz.dulaty.queue.core.common.SimplePage;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.department.data.entity.Department;
import org.springframework.data.domain.Pageable;

public interface GetDepartmentService {
    SimplePage<Department> getDepartments(Pageable pageable);
    Department getDepartmentById(Long id) throws NotFoundException;
}
