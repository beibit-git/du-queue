package kz.dulaty.queue.core.common.enums;

public enum CommonExceptionCode implements ExceptionCode{
    OBJECT_NOT_FOUND,
    USER_ALREADY_EXISTS,
    DATA_VALIDATION_FAILED,
    ACCESS_DENIED;
    private CommonExceptionCode() {
    }
}
