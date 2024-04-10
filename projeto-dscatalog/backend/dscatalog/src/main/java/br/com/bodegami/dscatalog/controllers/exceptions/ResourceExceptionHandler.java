package br.com.bodegami.dscatalog.controllers.exceptions;

import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

import static org.springframework.http.HttpStatus.*;

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

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(UNPROCESSABLE_ENTITY.value());
        err.setError("Validation Exception");
        err.setMessage(ex.getMessage());
        err.setPath(request.getRequestURI());

        for (FieldError f : ex.getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(err);
    }

    private ResponseEntity<StandardError> toErrorResponse(HttpStatus status, String errorMessage,
                                                          Exception ex, HttpServletRequest request) {
        StandardError errorResponse = new StandardError(
                Instant.now(),
                status.value(),
                errorMessage,
                ex.getMessage(),
                request.getRequestURI()



        );

        return ResponseEntity.status(status).body(errorResponse);
    }

}
