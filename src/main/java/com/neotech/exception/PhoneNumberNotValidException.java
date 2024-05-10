package com.neotech.exception;

public class PhoneNumberNotValidException extends RuntimeException {

    public static final String MESSAGE = "Invalid phone number provided";

    public PhoneNumberNotValidException(String message) {
        super(message);
    }
}
