package kz.dulaty.queue.feature.auth.data.enums;

public enum AuthErrorTypes {
    ALREADY_USED("Уже существует активная сессия"),
    ACC_LOCKED("Ваш аккаунт был заблокирован"),
    PASSWORD_EXPIRED("Срок действия вашего пароля истек"),
    MAX_PASS_COUNT("Достигнуто максимальное количество попыток входа"),
    INVALID_CREDENTIALS("Неправильный логин или пароль");

    AuthErrorTypes(String description) {
        this.description = description;
    }

    private final String description;
    public String getDescription() {
        return this.description;
    }
}
