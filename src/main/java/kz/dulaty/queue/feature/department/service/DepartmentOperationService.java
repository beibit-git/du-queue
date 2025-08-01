package kz.dulaty.queue.feature.department.service;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.department.data.dto.DepartmentRequestDto;

public interface DepartmentOperationService {
    void create(DepartmentRequestDto request);
    void update(Long id, DepartmentRequestDto request) throws NotFoundException;
    void delete(Long id) throws NotFoundException;
}
