package com.neotech.exception;

public class CountryNotFoundException extends RuntimeException {

    public static final String MESSAGE = "No country matches provided phone number";

    public CountryNotFoundException(String message) {
        super(message);
    }
}
