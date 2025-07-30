package kz.dulaty.queue.feature.auth.data.enums;

import lombok.Getter;

@Getter
public enum ConfirmationTokenType {

    EMAIL_CONFIRMATION (1440), PASSWORD_RESET(10);

    private final int expirationMinutes;

    ConfirmationTokenType(int expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }

}
