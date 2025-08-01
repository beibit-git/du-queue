package kz.dulaty.queue.feature.department.service.impl;

import kz.dulaty.queue.core.common.SimplePage;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.department.data.repository.DepartmentRepository;
import kz.dulaty.queue.feature.department.service.GetDepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDepartmentServiceImpl implements GetDepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public SimplePage<Department> getDepartments(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        return new SimplePage<>(departments.getContent(),
                pageable, departments.getTotalElements());
    }

    @Override
    public Department getDepartmentById(Long id) throws NotFoundException {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found with id: " + id));
    }
}
