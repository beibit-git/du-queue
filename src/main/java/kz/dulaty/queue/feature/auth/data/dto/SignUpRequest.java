package kz.dulaty.queue.feature.auth.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank(message = "Имя не может быть пустым")
        String name,
        @NotBlank(message = "Фамилия не может быть пустой")
        String surname,
        String patronymic,
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Email не валиден", flags = {Pattern.Flag.CASE_INSENSITIVE})
        String email,
        @NotBlank(message = "Пароль не может быть пустым")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$",
                flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE},
                message = "Пароль должен содержать минимум 8 символов, включая заглавную букву, цифру."
        )
        String password,
        @NotBlank(message = "Номер телефона не может быть пустым")
        @Pattern(
                regexp = "^\\d+$",
                flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE},
                message = "Номер телефона должен содержать только цифры."
        )
        String phoneNumber
) {
}
