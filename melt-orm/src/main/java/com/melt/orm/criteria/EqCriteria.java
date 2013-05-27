package com.melt.orm.criteria;

public class EqCriteria<T> implements Criteria{
    private String fieldName;
    private T fieldValue;

    public EqCriteria(String fieldName, T fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public String toExpression() {
        if (fieldValue instanceof String) {
            return fieldName + " = " + "'" + fieldValue + "'";
        }
        return fieldName + " = " + fieldValue.toString();
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
