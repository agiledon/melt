package com.melt.bean.constructor;

public class StringConstructorParameter extends  ConstructorParameter {
    public StringConstructorParameter(int index, String value) {
        super(index);
        setValue(value);
    }
}
