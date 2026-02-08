package com.prueba.seti.api_test.infrastructure.adapter.in.rest.exception;

import com.prueba.seti.api_test.domain.exception.BusinessValidationException;
import com.prueba.seti.api_test.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;

@RestControllerAdvice
@Order(-2)
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception ex, ServerWebExchange exchange) {
        String message = ex instanceof MethodArgumentNotValidException manve
            ? manve.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validacion invalida")
            : ex.getMessage();
        return buildError(HttpStatus.BAD_REQUEST, message, exchange);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessValidationException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), exchange);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno", exchange);
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, ServerWebExchange exchange) {
        ErrorResponse response = new ErrorResponse(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            exchange.getRequest().getPath().value()
        );
        return ResponseEntity.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }
}

