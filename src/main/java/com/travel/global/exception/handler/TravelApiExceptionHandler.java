package com.travel.global.exception.handler;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice(basePackages = "com.travel.domain")
@Order(value = 1)
public class TravelApiExceptionHandler {

    private final Logger warnLogger = LoggerFactory.getLogger("warnLogger");

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<?> travelExceptionAdvice(HttpStatusCodeException e) {
        log.error("error message : {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).build();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            warnLogger.error("======= Validation Exception =======");
            warnLogger.error("Exception type: {}", e.getClass().getSimpleName());
            warnLogger.error("Exception message: {}", fieldError.getDefaultMessage());
        });

        String errorMessage = String.join(", ", errors.values());
        log.error("error message : {}", errorMessage);

        return ResponseEntity.status(e.getStatusCode()).build();
    }
}
