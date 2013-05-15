package com.melt.exceptions;

public class MoreThanOneBeanWithSameClass extends RuntimeException {
    public MoreThanOneBeanWithSameClass(String className) {
        super(className);
    }
}
