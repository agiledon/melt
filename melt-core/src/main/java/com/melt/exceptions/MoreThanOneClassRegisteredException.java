package com.melt.exceptions;

public class MoreThanOneClassRegisteredException extends RuntimeException {
    public MoreThanOneClassRegisteredException(String message) {
        super(message);
    }
}
