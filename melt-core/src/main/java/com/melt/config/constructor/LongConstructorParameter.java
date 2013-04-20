package com.melt.config.constructor;

public class LongConstructorParameter extends ConstructorParameter {
    public LongConstructorParameter(int index, long value) {
        super(index);
        setValue(value);
    }
}
