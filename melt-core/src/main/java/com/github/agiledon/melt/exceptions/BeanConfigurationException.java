package com.github.agiledon.melt.exceptions;

public class BeanConfigurationException extends RuntimeException {
    private String message;

    public BeanConfigurationException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
