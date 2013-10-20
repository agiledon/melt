package com.github.agiledon.melt.exceptions;

public class InitBeanException extends RuntimeException {
    private String message;

    public InitBeanException(String message) {
        super(message);
        this.message = message;
    }

    public InitBeanException(String message, Throwable e) {
        super(e);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
