package kz.dulaty.queue.core.exception;

public class CustomAuthenticationException extends Exception{
    public CustomAuthenticationException() {
        super();
    }

    public CustomAuthenticationException(String message) {
        super(message);
    }

    public CustomAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected CustomAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
