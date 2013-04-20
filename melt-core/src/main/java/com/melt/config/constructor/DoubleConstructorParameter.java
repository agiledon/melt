package com.melt.config.constructor;

public class DoubleConstructorParameter extends ConstructorParameter {
    public DoubleConstructorParameter(int index, double value) {
        super(index);
        setValue(value);
    }
}
