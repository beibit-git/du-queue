package kz.dulaty.queue.feature.manager.service;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.feature.manager.data.dto.AddManagerDto;

public interface ManagerOperationService {
    void addManager(AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException;
    void deleteManager(Long managerId) throws NotFoundException;
    void updateManager(Long managerId, AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException;
    void inActivateManager(Long managerId) throws NotFoundException;
    void activateManager(Long managerId) throws NotFoundException;
}
