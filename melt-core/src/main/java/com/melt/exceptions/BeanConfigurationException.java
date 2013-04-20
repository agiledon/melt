package com.melt.exceptions;

public class BeanConfigurationException extends RuntimeException {
    private String message;

    public BeanConfigurationException(String message) {
        this.message = message;
    }

    public BeanConfigurationException(String message, Throwable e) {
        super(e);
        this.message = message;
    }
}
