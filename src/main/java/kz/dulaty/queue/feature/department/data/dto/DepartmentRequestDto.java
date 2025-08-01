package kz.dulaty.queue.feature.department.data.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String fullName,
        @NotBlank
        String prefix
) {
}