package kz.dulaty.queue.feature.manager.service;

import kz.dulaty.queue.core.common.SimplePage;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.manager.data.dto.ManagerDto;
import kz.dulaty.queue.feature.manager.data.dto.ManagerFilter;
import org.springframework.data.domain.Pageable;

public interface GetManagerService {
    SimplePage<ManagerDto> getAll(ManagerFilter filter, Pageable pageable);

    ManagerDto getById(Long id) throws NotFoundException;
}
