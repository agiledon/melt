package com.melt.orm.criteria;

public abstract class BinaryCriteria implements Criteria {
    protected Criteria leftCriteria;
    protected Criteria rightCriteria;

    public BinaryCriteria(Criteria leftCriteria, Criteria rightCriteria) {
        this.leftCriteria = leftCriteria;
        this.rightCriteria = rightCriteria;
    }

    @Override
    public String toExpression() {
        return leftCriteria.toExpression() + getOperator() + rightCriteria.toExpression();
    }

    @Override
    public boolean isNull() {
        return false;
    }

    protected abstract String getOperator();
}
