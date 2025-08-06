package kz.dulaty.queue.feature.department.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.dulaty.queue.core.common.SimplePage;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.department.data.dto.DepartmentRequestDto;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.department.service.DepartmentOperationService;
import kz.dulaty.queue.feature.department.service.GetDepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Отделы")
@RestController
@RequestMapping("/api/v1/queue/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentOperationService operationService;
    private final GetDepartmentService getDepartmentService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public SimplePage<Department> getAll(@SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                         @PageableDefault(size = 20) final Pageable pageable) {
        return getDepartmentService.getDepartments(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public Department getById(@PathVariable Long id) throws NotFoundException {
        return getDepartmentService.getDepartmentById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void create(@RequestBody @Valid DepartmentRequestDto department) {
        operationService.create(department);
        log.info("Department created: {}", department);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void update(@PathVariable Long id,
                       @RequestBody DepartmentRequestDto department) throws NotFoundException {
        operationService.update(id, department);
        log.info("Department updated: {}", department);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public void delete(@PathVariable Long id) throws NotFoundException {
        operationService.delete(id);
        log.info("Department deleted with id: {}", id);
    }
}
