package com.github.agiledon.melt.exceptions;

public class MoreThanOneBeanWithSameClass extends RuntimeException {
    public MoreThanOneBeanWithSameClass(String className) {
        super(className);
    }
}
