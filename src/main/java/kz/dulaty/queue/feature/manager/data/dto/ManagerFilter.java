package kz.dulaty.queue.feature.manager.data.dto;

public record ManagerFilter(
        String keyword,
        String departmentId,
        String windowNumber
) {
}
