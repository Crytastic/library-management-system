package cz.muni.fi.pa165.exceptionhandling.exceptions;

public class UnableToContactServiceException extends RuntimeException {

    public UnableToContactServiceException() {
    }

    public UnableToContactServiceException(String message) {
        super(message);
    }

    public UnableToContactServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToContactServiceException(Throwable cause) {
        super(cause);
    }
}