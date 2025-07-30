package kz.dulaty.queue.feature.auth.data.dto;

import jakarta.validation.constraints.NotBlank;


public record SignInRequestDto(
        @NotBlank(message = "Email не может быть пустым")
        String email,
        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}
