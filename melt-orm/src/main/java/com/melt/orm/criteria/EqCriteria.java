package com.melt.orm.criteria;

import com.melt.orm.util.FieldValueWrapper;

import static com.melt.orm.util.FieldValueWrapper.wrap;

public class EqCriteria<T> extends SingleCriteria{

    public EqCriteria(String fieldName, T fieldValue) {
        super(fieldName, fieldValue);
    }

    @Override
    public String toExpression() {
        return fieldName + getOperator() + wrap(fieldValue);
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
