package cz.muni.fi.pa165.exceptionhandling;

import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceAlreadyExistsException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UrlPathHelper;

import java.time.Clock;
import java.time.LocalDateTime;

@RestControllerAdvice
/**
 * Taken from seminar-spring-boot-testing.
 *
 * @authoer Adam Krídl
 */
public class CustomRestGlobalExceptionHandling {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ApiError> handleResourceNotFound(final ResourceNotFoundException ex, final HttpServletRequest request) {
        final ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleResourceAlreadyExists(final ResourceAlreadyExistsException ex, final HttpServletRequest request) {
        final ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ApiError> handleResourceAlreadyExists(final UnauthorizedException ex, final HttpServletRequest request) {
        final ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.UNAUTHORIZED,
                ex.getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all the exceptions not matched by above-mentioned definitions.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleAll(final Exception ex, HttpServletRequest request) {
        final ApiError apiError = new ApiError(
                LocalDateTime.now(Clock.systemUTC()),
                HttpStatus.INTERNAL_SERVER_ERROR,
                getInitialException(ex).getLocalizedMessage(),
                URL_PATH_HELPER.getRequestUri(request));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private Exception getInitialException(Exception ex) {
        while (ex.getCause() != null) {
            ex = (Exception) ex.getCause();
        }
        return ex;
    }

}
