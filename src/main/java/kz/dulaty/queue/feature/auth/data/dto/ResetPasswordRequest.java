package kz.dulaty.queue.feature.auth.data.dto;

import kz.dulaty.queue.feature.auth.data.validators.Password;

public record ResetPasswordRequest(
        @Password
        String password,
        @Password
        String confirmPassword
) {
}
