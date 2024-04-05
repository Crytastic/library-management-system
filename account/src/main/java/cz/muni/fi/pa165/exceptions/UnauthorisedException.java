package cz.muni.fi.pa165.exceptions;

/**
 * Custom exception dealing with system unauthorized state
 *
 * @author Sophia Zápotočná
 */
public class UnauthorisedException extends RuntimeException {
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
