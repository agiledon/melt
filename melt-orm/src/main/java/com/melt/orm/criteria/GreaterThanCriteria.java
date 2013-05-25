package com.melt.orm.criteria;

public class GreaterThanCriteria<T> extends SingleCriteria<T> {

    public GreaterThanCriteria(String fieldName, T fieldValue) {
        super(fieldName, fieldValue);
    }

    @Override
    protected String getOperator() {
        return " > ";
    }

}
