package com.salihyusufcankurt.document_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body(Map.of(
                "error","VALIDATION_ERROR",
                "details", ex.getBindingResult().toString()
        ));
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApi(ApiException ex){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of(
                "error","API_ERROR",
                "message", ex.getMessage()
        ));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error","INTERNAL_ERROR",
                "message", ex.getMessage()
        ));
    }
}