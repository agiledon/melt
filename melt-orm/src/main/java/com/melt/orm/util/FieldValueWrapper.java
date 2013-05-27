package com.melt.orm.util;

public class FieldValueWrapper {
    public static <T> String wrap(T fieldValue) {
        if (fieldValue == null) {
            return "null";
        }
        if (fieldValue instanceof String) {
            return "'" + fieldValue + "'";
        }
        return fieldValue.toString();
    }
}