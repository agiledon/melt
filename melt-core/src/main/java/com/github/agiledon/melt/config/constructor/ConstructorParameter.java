package com.github.agiledon.melt.config.constructor;

import com.github.agiledon.melt.core.InjectionContext;

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
    public void updateValue(InjectionContext injectionContext) {
        doNothing();
    }

    private void doNothing() {
    }
}
