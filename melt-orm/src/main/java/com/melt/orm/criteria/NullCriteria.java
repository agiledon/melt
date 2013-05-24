package com.melt.orm.criteria;

public class NullCriteria implements Criteria {
    @Override
    public String toExpression() {
        return "";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
