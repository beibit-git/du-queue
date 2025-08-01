package kz.dulaty.queue.feature.manager.service.impl;

import kz.dulaty.queue.core.common.SimplePage;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.manager.data.dto.ManagerDto;
import kz.dulaty.queue.feature.manager.data.dto.ManagerFilter;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import kz.dulaty.queue.feature.manager.data.mapper.ManagerMapper;
import kz.dulaty.queue.feature.manager.data.repository.ManagerRepository;
import kz.dulaty.queue.feature.manager.data.specification.ManagerSpecification;
import kz.dulaty.queue.feature.manager.service.GetManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetManagerServiceImpl implements GetManagerService {
    private final ManagerRepository managerRepository;

    @Override
    public SimplePage<ManagerDto> getAll(ManagerFilter filter, Pageable pageable) {

        Specification<Manager> specification = ManagerSpecification.builder()
                .filter(filter)
                .build();
        Page<Manager> managerPage = managerRepository.findAll(specification, pageable);
        return new SimplePage<>(
                managerPage.getContent().stream()
                        .map(ManagerMapper.MANAGER_MAPPER::toDto)
                        .toList(),
                pageable,
                managerPage.getTotalElements()
        );
    }

    @Override
    public ManagerDto getById(Long id) throws NotFoundException {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Manager not found with id: " + id));
        return ManagerMapper.MANAGER_MAPPER.toDto(manager);
    }
}
