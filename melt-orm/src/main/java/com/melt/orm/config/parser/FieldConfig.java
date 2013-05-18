package com.melt.orm.config.parser;

import static com.google.common.base.Preconditions.checkNotNull;

public class FieldConfig {
    private String fieldName;
    private Class filedType;
    private String fieldType;

    public FieldConfig(String fieldName, Class filedType) {
        checkNotNull(fieldName);
        checkNotNull(filedType);
        this.fieldName = fieldName;
        this.filedType = filedType;
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

    public String getFieldType() {
        return fieldType;
    }
}
