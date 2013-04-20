package com.melt.config.property;

public class PropertyValue {
    private int intValue;
    private String stringValue;
    private Object refValue;
    private boolean integerType = false;
    private boolean stringType = false;

    public PropertyValue(int propertyValue) {
        integerType = true;
        intValue = propertyValue;
    }

    public PropertyValue(String propertyValue) {
        stringType = true;
        stringValue = propertyValue;
    }

    public PropertyValue(Object refValue) {
        this.refValue = refValue;
    }

    public int getInt() {
        return intValue;
    }

    public String getString() {
        return stringValue;
    }

    public Object getRefValue() {
        return refValue;
    }

    public boolean isInteger() {
        return integerType;
    }

    public boolean isString() {
        return stringType;
    }
}
