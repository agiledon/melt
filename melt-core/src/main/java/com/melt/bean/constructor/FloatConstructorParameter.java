package com.melt.bean.constructor;

public class FloatConstructorParameter extends ConstructorParameter {
    public FloatConstructorParameter(int index, float value) {
        super(index);
        setValue(value);
    }
}
