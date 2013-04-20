package com.melt.bean.constructor;

public class IntConstructorParameter extends ConstructorParameter{
    public IntConstructorParameter(int index, int value) {
        super(index);
        this.setValue(value);
    }
}
