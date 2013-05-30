package com.melt.orm.util;

public class FieldValueWrapper {
    public static <T> String wrap(T fieldValue) {
        if (fieldValue == null) {
            return "null";
        }
        if (fieldValue instanceof String || fieldValue instanceof Enum) {
            return "'" + fieldValue + "'";
        }
        if (fieldValue instanceof Boolean) {
            Boolean booleanValue = (Boolean) fieldValue;
            return booleanValue ? "1" : "0";
        }
        return fieldValue.toString();
    }
}