package com.mphasis.cqrs.os.command.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ResponseError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date timestamp;
    private String message;

    private ResponseError() {
        timestamp = new Date();
    }

    ResponseError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = ex.getMessage();
    }

    ResponseError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}
