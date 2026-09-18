package com.driver.exception;

import com.driver.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CouldNotFoundException.class)
    public ResponseEntity<?> handleDriverNotFoundException(CouldNotFoundException exception) {
        ErrorResponse driverNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), "Specific info of driver could not be found");
        return new ResponseEntity<>(driverNotFound, HttpStatus.NOT_FOUND);
    }
}

