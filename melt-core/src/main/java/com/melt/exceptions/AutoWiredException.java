package com.melt.exceptions;

public class AutoWiredException extends RuntimeException {
    private String message;

    public AutoWiredException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
