package kz.dulaty.queue.feature.manager.service;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.feature.manager.data.dto.AddManagerDto;

public interface ManagerOperationService {
    void addManager(AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException;
    void deleteManager(Long managerId);
    void updateManager(Long managerId, AddManagerDto addManagerDto);
    void inActivateManager(Long managerId);
    void activateManager(Long managerId);
}
