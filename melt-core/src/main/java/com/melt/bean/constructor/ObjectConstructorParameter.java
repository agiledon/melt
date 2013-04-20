package com.melt.bean.constructor;

public class ObjectConstructorParameter extends ConstructorParameter {
    public ObjectConstructorParameter(int index, Object value) {
        super(index);
        setValue(value);
    }
}
