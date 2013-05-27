package com.melt.orm.criteria;

import com.melt.orm.util.NameMapping;

public class EqCriteria<T> extends SingleCriteria{

    public EqCriteria(String fieldName, T fieldValue) {
        super(fieldName, fieldValue);
    }

    @Override
    public String toExpression() {
        String targetValue = fieldValue.toString();
        if (fieldValue instanceof String) {
            targetValue = "'" + fieldValue + "'";
        }
        return fieldName + getOperator() + targetValue;
    }

    @Override
    protected String getOperator() {
        return " = ";
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
