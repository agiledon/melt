package com.melt.orm.criteria;

public class NullCriteria implements Criteria {
    @Override
    public String evaluate() {
        return "";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
