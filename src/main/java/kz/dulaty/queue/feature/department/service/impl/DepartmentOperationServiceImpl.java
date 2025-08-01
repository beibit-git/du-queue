package kz.dulaty.queue.feature.department.service.impl;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.department.data.DepartmentMapper;
import kz.dulaty.queue.feature.department.data.dto.DepartmentRequestDto;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.department.data.repository.DepartmentRepository;
import kz.dulaty.queue.feature.department.service.DepartmentOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentOperationServiceImpl implements DepartmentOperationService {
    private final DepartmentRepository departmentRepository;

    @Override
    public void create(DepartmentRequestDto request) {

        Department department = DepartmentMapper.DEPARTMENT_MAPPER.toEntity(request);
        departmentRepository.save(department);

        log.info("Department with id {} created successfully", department.getId());
    }

    @Override
    public void update(Long id, DepartmentRequestDto request) throws NotFoundException {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found"));

        DepartmentMapper.DEPARTMENT_MAPPER.updateEntity(department, request);

        departmentRepository.save(department);

        log.info("Department with id {} updated successfully", id);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        if (!departmentRepository.existsById(id)) {
            throw new NotFoundException("Department not found");
        }

        departmentRepository.deleteById(id);

        log.info("Department with id {} deleted successfully", id);
    }
}
