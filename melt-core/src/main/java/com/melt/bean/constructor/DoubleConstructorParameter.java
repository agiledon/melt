package com.melt.bean.constructor;

public class DoubleConstructorParameter extends ConstructorParameter {
    public DoubleConstructorParameter(int index, double value) {
        super(index);
        setValue(value);
    }
}
