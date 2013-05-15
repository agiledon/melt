package com.melt.config.constructor;

import com.melt.core.Container;
import com.melt.core.InitializedBeans;

public class ConstructorParameter implements ParameterValueUpdater {
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

    @Override
    public void updateValue(Container parentContainer, InitializedBeans container) {
        doNothing();
    }

    private void doNothing() {
    }
}
