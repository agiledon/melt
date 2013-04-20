package com.melt.bean.constructor;

public class ConstructorParameter {
    protected int index;
    private Object value;

    public ConstructorParameter(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
