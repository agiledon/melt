package com.melt.orm.criteria;

public class LessThanCriteria<T> extends SingleCriteria {
    public LessThanCriteria(String fieldName, Object fieldValue) {
        super(fieldName, fieldValue);
    }

    @Override
    protected String getOperator() {
        return " < ";
    }
}
