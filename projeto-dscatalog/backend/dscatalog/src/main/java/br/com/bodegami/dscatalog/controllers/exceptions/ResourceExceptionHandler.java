package br.com.bodegami.dscatalog.controllers.exceptions;

import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return toErrorResponse(NOT_FOUND, "Resource not found!", ex, request);
    }

    @ExceptionHandler(value = {DatabaseException.class})
    public ResponseEntity<StandardError> database(DatabaseException ex, HttpServletRequest request) {
        return toErrorResponse(BAD_REQUEST, "Database Exception!", ex, request);
    }

    private ResponseEntity<StandardError> toErrorResponse(HttpStatus status, String errorMessage,
                                                          Exception ex, HttpServletRequest request) {
        StandardError errorResponse = new StandardError(
                Instant.now(), status.value(), errorMessage, ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(errorResponse);
    }

}
