package com.melt.exceptions;

public class MoreThanOneBeanWithSameClass extends RuntimeException {
    private String className;

    public MoreThanOneBeanWithSameClass(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
