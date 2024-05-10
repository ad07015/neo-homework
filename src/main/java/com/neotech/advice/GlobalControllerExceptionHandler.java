package com.neotech.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public ResponseEntity<String> handleConversion(PhoneNumberNotValidException ex) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(ex.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<String> handleMyCustomException(CountryNotFoundException ex) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(ex.getMessage()), NOT_FOUND);
    }
}
