package org.course.registration.exception;

public class NotEnoughException extends RuntimeException{
    public NotEnoughException() {
        super();
    }

    public NotEnoughException(String message) {
        super(message);
    }

    public NotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughException(Throwable cause) {
        super(cause);
    }
}
