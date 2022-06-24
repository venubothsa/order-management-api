package com.mphasis.cqrs.os.command.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class OrderExceptionHandler {
    private final static String PRE_CONDITION_FAILED = "Pre Condition Failed...!";

    @ExceptionHandler(PreconditionFailed.class)
    public ResponseEntity<Object> handlePreConditionException(PreconditionFailed ex) {
        log.error(PRE_CONDITION_FAILED);
        return buildResponseEntity(
                new ResponseError(HttpStatus.BAD_REQUEST, PRE_CONDITION_FAILED)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationsException(MethodArgumentNotValidException ex) {
        log.error(PRE_CONDITION_FAILED);
        return buildResponseEntity(
                new ResponseError(HttpStatus.BAD_REQUEST, PRE_CONDITION_FAILED)
        );
    }

    private ResponseEntity<Object> buildResponseEntity(ResponseError responseError) {
        return new ResponseEntity<>(responseError, responseError.getStatus());
    }

}
