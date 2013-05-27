package com.melt.orm.util;

public class FieldValueWrapper {
    public static <T> String wrap(T fieldValue1) {
        if (fieldValue1 instanceof String) {
            return "'" + fieldValue1 + "'";
        }
        return fieldValue1.toString();
    }
}