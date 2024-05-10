package com.neotech.exception;

public class PhoneNumberNotValidException extends RuntimeException {

    public PhoneNumberNotValidException(String message) {
        super(message);
    }
}
