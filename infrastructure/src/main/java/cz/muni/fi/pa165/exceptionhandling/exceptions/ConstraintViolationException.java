package cz.muni.fi.pa165.exceptionhandling.exceptions;

public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException() {
    }

    public ConstraintViolationException(String message) {
        super(message);
    }

    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintViolationException(Throwable cause) {
        super(cause);
    }
}
