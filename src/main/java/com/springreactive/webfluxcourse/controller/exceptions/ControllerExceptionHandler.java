package com.springreactive.webfluxcourse.controller.exceptions;

import com.springreactive.webfluxcourse.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicatedKeyException(DuplicateKeyException ex, ServerHttpRequest request) {
        return ResponseEntity.badRequest()
                .body(Mono.just(
                                StandardError.builder()
                                        .timestamp(LocalDateTime.now())
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                        .errorMessage(verifyDupKey(ex.getMessage()))
                                        .path(request.getPath().toString())
                                        .build()
                        )
                );
    }

    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "Email already registered";
        } else {
            return "Dup key exception!";
        }
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> validationError(WebExchangeBindException ex, ServerHttpRequest request) {
        ValidationError error = new ValidationError(LocalDateTime.now(),
                request.getPath().toString(), HttpStatus.BAD_REQUEST.value(),
                "Validation Error", "Error on attributes validation");
        for (FieldError fe : ex.getBindingResult().getFieldErrors()){
            error.addErrors(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(error));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<Mono<StandardError>> objectNotFoundException(ObjectNotFoundException ex, ServerHttpRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Mono.just(
                                StandardError.builder()
                                        .timestamp(LocalDateTime.now())
                                        .status(HttpStatus.NOT_FOUND.value())
                                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                        .errorMessage(ex.getMessage())
                                        .path(request.getPath().toString())
                                        .build()
                        )
                );
    }
}