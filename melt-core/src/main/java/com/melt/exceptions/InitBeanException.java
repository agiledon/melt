package com.melt.exceptions;

public class InitBeanException extends RuntimeException {
    private String message;

    public InitBeanException(String message) {
        this.message = message;
    }

    public InitBeanException(String message, Exception e) {
        super(e);
        this.message = message;
    }
}
