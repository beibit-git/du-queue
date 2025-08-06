package kz.dulaty.queue.feature.manager.service.impl;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.auth.data.enums.SafetyRole;
import kz.dulaty.queue.feature.auth.service.UserService;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.department.data.repository.DepartmentRepository;
import kz.dulaty.queue.feature.manager.data.dto.AddManagerDto;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import kz.dulaty.queue.feature.manager.data.mapper.ManagerMapper;
import kz.dulaty.queue.feature.manager.data.repository.ManagerRepository;
import kz.dulaty.queue.feature.manager.service.ManagerOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerOperationServiceImpl implements ManagerOperationService {
    private final ManagerRepository managerRepository;
    private final DepartmentRepository departmentRepository;
    private final UserService userService;

    @Override
    public void addManager(AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException {
        User user = userService.signUp(addManagerDto.user(), SafetyRole.MANAGER);

        Department department = departmentRepository.findById(addManagerDto.departmentId())
                .orElseThrow(() -> new NotFoundException("Department not found"));

        Manager manager = ManagerMapper.MANAGER_MAPPER.toEntity(addManagerDto.windowNumber(), department, user);
        managerRepository.save(manager);

        log.info("Manager with ID {} has been added to department with ID {}", manager.getId(), department.getId());
    }

    @Override
    public void deleteManager(Long managerId) throws NotFoundException {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found"));
        managerRepository.delete(manager);
        log.info("Manager with ID {} has been deleted", managerId);
    }

    @Override
    public void updateManager(Long managerId, AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found"));

        Department department = departmentRepository.findById(addManagerDto.departmentId())
                .orElseThrow(() -> new NotFoundException("Department not found"));

        User user = userService.updateUserInfo(
                manager.getUser().getId(), addManagerDto.user());

        ManagerMapper.MANAGER_MAPPER.updateEntity(
                manager,
                addManagerDto.windowNumber(),
                department,
                user
        );

        managerRepository.save(manager);

        log.info("Manager with ID {} has been updated", managerId);
    }

    @Override
    public void inActivateManager(Long managerId) throws NotFoundException {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found"));
        manager.setIsActive(false);
        managerRepository.save(manager);
        log.info("Manager with ID {} has been deactivated", managerId);
    }

    @Override
    public void activateManager(Long managerId) throws NotFoundException {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("Manager not found"));
        manager.setIsActive(false);
        managerRepository.save(manager);
        log.info("Manager with ID {} has been activated", managerId);
    }
}
