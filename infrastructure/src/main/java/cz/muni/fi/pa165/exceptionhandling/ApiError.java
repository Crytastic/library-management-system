package cz.muni.fi.pa165.exceptionhandling;


import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String path;

    /**
     * Class representing different API errors from services.
     * Taken from seminar-spring-boot-testing
     *
     * @author Adam Krídl
     */
    public ApiError() {
    }

    public ApiError(LocalDateTime timestamp, HttpStatus status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
