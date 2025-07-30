package kz.dulaty.queue.feature.auth.data.dto;

import java.util.List;

public record AddUserRequestDto(
        String firstName,
        String lastName,
        String patronymic,
        String email,
        String password,
        String phoneNumber,
        List<Long> roles
) {
}
