package com.melt.bean.constructor;

public class GenericConstructorParameter<T> extends ConstructorParameter {
    public GenericConstructorParameter(int index, T value) {
        super(index);
        setValue(value);
    }
}
