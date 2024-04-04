package cz.muni.fi.pa165.exception;

public class UnauthorisedException extends Exception {
    public UnauthorisedException() {
    }

    public UnauthorisedException(String message) {
        super(message);
    }

    public UnauthorisedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorisedException(Throwable cause) {
        super(cause);
    }

}
