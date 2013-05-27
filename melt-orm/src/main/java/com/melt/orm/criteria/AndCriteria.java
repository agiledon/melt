package com.melt.orm.criteria;

public class AndCriteria extends BinaryCriteria {
    public AndCriteria(Criteria leftCriteria, Criteria rightCriteria) {
        super(leftCriteria, rightCriteria);
    }

    @Override
    protected String getOperator() {
        return " AND ";
    }

}
