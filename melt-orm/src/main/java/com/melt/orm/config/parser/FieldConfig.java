package com.melt.orm.config.parser;

import static com.google.common.base.Preconditions.checkNotNull;

public class FieldConfig {
    private String fieldName;
    private Class fieldType;
    private boolean setType;
    private Class genericType;
    private boolean oneToOne;
    private boolean manyToOne;
    private boolean oneToMany;

    public FieldConfig(String fieldName, Class fieldType) {
        this(fieldName, fieldType, false, null);
    }

    public FieldConfig(String fieldName, Class fieldType, boolean setType, Class genericType) {
        checkNotNull(fieldName);
        checkNotNull(fieldType);
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.setType = setType;
        this.genericType = genericType;
    }

    public boolean isPrimaryKeyField() {
        if (fieldName.endsWith("id") || fieldName.endsWith("Id")) {
            return true;
        }
        return false;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public boolean isSetType() {
        return setType;
    }

    public Class getGenericType() {
        return genericType;
    }

    public void setOneToOne(boolean oneToOne) {
        this.oneToOne = oneToOne;
    }

    public boolean isOneToOneField() {
        return oneToOne;
    }

    public void setManyToOne(boolean manyToOne) {
        this.manyToOne = manyToOne;
    }

    public boolean isManyToOneField() {
        return manyToOne;
    }

    public boolean isOneToManyField() {
        return oneToMany;
    }

    public void setOneToMany(boolean oneTOMany) {
        this.oneToMany = oneTOMany;
    }
}
