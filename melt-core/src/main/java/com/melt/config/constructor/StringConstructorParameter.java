package com.melt.config.constructor;

public class StringConstructorParameter extends  ConstructorParameter {
    public StringConstructorParameter(int index, String value) {
        super(index);
        setValue(value);
    }
}
