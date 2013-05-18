package com.melt.orm.config.parser;

import static com.google.common.base.Preconditions.checkNotNull;

public class FieldConfig {
    private String fieldName;
    private Class fieldType;

    public FieldConfig(String fieldName, Class fieldType) {
        checkNotNull(fieldName);
        checkNotNull(fieldType);
        this.fieldName = fieldName;
        this.fieldType = fieldType;
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
}
