package kz.dulaty.queue.feature.manager.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.dulaty.queue.core.common.SimplePage;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.feature.manager.data.dto.AddManagerDto;
import kz.dulaty.queue.feature.manager.data.dto.ManagerDto;
import kz.dulaty.queue.feature.manager.data.dto.ManagerFilter;
import kz.dulaty.queue.feature.manager.service.GetManagerService;
import kz.dulaty.queue.feature.manager.service.ManagerOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Менеджеры")
@RestController
@RequestMapping("/api/v1/queue/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerOperationService managerOperationService;
    private final GetManagerService getManagerService;

    @GetMapping
    public SimplePage<ManagerDto> getAll(@SortDefault(sort = "id")
                                         @PageableDefault(size = 20) final Pageable pageable,
                                         @ModelAttribute ManagerFilter managerFilter) {
        log.info("Fetching all managers with filter: {}", managerFilter);
        return getManagerService.getAll(managerFilter, pageable);
    }

    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable Long id) throws NotFoundException {
        log.info("Fetching manager with id: {}", id);
        return getManagerService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void addManager(@RequestBody @Valid AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException {
        log.info("Creating a new manager");
        managerOperationService.addManager(addManagerDto);
        log.info("Manager created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void updateManager(@PathVariable Long id,
                              @RequestBody AddManagerDto addManagerDto) throws NotFoundException, UserAlreadyExistsException {
        log.info("Updating manager with id: {}", id);
        managerOperationService.updateManager(id, addManagerDto);
        log.info("Manager updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void deleteManager(@PathVariable Long id) throws NotFoundException {
        log.info("Deleting manager with id: {}", id);
        managerOperationService.deleteManager(id);
        log.info("Manager deleted successfully");
    }

    @GetMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void deactivateManager(@PathVariable Long id) throws NotFoundException {
        log.info("Deactivating manager with id: {}", id);
        managerOperationService.inActivateManager(id);
        log.info("Manager deactivated successfully");
    }

    @GetMapping("/activate/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void activateManager(@PathVariable Long id) throws NotFoundException {
        log.info("Activating manager with id: {}", id);
        managerOperationService.activateManager(id);
        log.info("Manager activated successfully");
    }
}
