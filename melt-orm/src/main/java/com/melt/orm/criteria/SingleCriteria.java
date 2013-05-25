package com.melt.orm.criteria;

public abstract class SingleCriteria<T> implements Criteria {
    protected String fieldName;
    protected T fieldValue;

    public SingleCriteria(String fieldName, T fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public String toExpression() {
        return fieldName + getOperator() + fieldValue.toString();
    }

    protected abstract String getOperator();

    @Override
    public boolean isNull() {
        return false;
    }
}
