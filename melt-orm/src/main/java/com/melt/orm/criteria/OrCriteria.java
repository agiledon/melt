package com.melt.orm.criteria;

public class OrCriteria extends BinaryCriteria {
    public OrCriteria(Criteria leftCriteria, Criteria rightCriteria) {
        super(leftCriteria, rightCriteria);
    }

    @Override
    protected String getOperator() {
        return " or ";
    }
}
