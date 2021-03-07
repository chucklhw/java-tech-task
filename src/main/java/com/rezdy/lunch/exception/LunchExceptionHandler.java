package com.rezdy.lunch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class LunchExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(DateTimeParseException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage("Erroneous format of the date parameter");
        return new ResponseEntity(errorResponse.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

}
