package kz.dulaty.queue.feature.manager.data.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.dulaty.queue.feature.auth.data.dto.UpdateUserRequest;

public record UpdateManagerDto(
        @NotNull @Valid
        UpdateUserRequest user,
        @NotNull
        Long departmentId,
        @NotBlank
        String windowNumber
) {
}
