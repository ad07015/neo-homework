package com.neotech.advice;

import com.neotech.exception.CountryNotFoundException;
import com.neotech.exception.PhoneNumberNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(PhoneNumberNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> handleConversion(PhoneNumberNotValidException ex) {
        return new ResponseEntity<>("Invalid phone number provided", BAD_REQUEST);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<String> handleMyCustomException(CountryNotFoundException ex) {
        return new ResponseEntity<>("No country matches provided phone number", NOT_FOUND);
    }
}
