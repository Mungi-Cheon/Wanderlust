package com.travel.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice(basePackages = "com.travel.domain")
@Order(value = 1)
public class TravelApiExceptionHandler {

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<?> travelExceptionAdvice(HttpStatusCodeException e) {
        log.error("error message : {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).build();
    }
}
