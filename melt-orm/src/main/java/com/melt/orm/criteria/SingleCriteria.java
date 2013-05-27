package com.melt.orm.criteria;

import com.melt.orm.util.NameMapping;

public abstract class SingleCriteria<T> implements Criteria {
    protected String fieldName;
    protected T fieldValue;

    public SingleCriteria(String fieldName, T fieldValue) {
        this.fieldName = NameMapping.getMappedName(fieldName);
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
