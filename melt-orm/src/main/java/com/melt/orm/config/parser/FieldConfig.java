package com.melt.orm.config.parser;

import com.google.common.base.CaseFormat;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

public class FieldConfig {
    private final String columnName;
    private String fieldName;
    private Class fieldType;
    private boolean setType;
    private Class genericType;
    private boolean oneToOne;
    private boolean manyToOne;
    private boolean oneToMany;
    private String referenceColumnName;

    public Method getReader() {
        return reader;
    }

    public void setReader(Method reader) {
        this.reader = reader;
    }

    public Method getWriter() {
        return writer;
    }

    public void setWriter(Method writer) {
        this.writer = writer;
    }

    private Method reader;
    private Method writer;

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
        this.columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName) + "_ID";
    }

    public boolean isPrimaryKeyField() {
        if (fieldName.endsWith("id") || fieldName.endsWith("Id")) {
            return true;
        }
        return false;
    }

    public boolean isNeedBeProxy(){
        return manyToOne || oneToMany || oneToOne;
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
        this.referenceColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName) + "_ID";
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

    public String getReferenceColumnName() {
        return referenceColumnName;
    }
}
