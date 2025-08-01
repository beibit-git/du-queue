package kz.dulaty.queue.feature.manager.data.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.dulaty.queue.feature.auth.data.dto.SignUpRequest;

public record AddManagerDto(
        @NotNull @Valid
        SignUpRequest user,
        @NotNull
        Long departmentId,
        @NotBlank
        String windowNumber
) {
}
