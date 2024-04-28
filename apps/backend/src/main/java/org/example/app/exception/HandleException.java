package org.example.app.exception;


import org.apache.coyote.BadRequestException;
import org.example.app.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class HandleException {
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorModel> handleBadRequestsException(BadRequestException ex){
        ErrorModel errorModel = new ErrorModel(ex.getMessage(), HttpStatus.BAD_REQUEST, new ArrayList<>());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel);
    }

    @ExceptionHandler(BindException.class)
    public final ResponseEntity<?> handleBindException(BindException ex){
        ErrorModel errorModel = new ErrorModel(ex.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST, new ArrayList<>());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;
        exception.printStackTrace();

        return errorDetail;
    }

}
