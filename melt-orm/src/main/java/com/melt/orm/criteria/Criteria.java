package com.melt.orm.criteria;

public interface Criteria {
    public String toExpression();
    public boolean isNull();
}
