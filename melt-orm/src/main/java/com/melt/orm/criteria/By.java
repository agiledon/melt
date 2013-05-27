package com.melt.orm.criteria;

public class By {
    public static Criteria nil(){
        return new NullCriteria();
    }

    public static Criteria id(int id) {
        return new EqCriteria("id", id);
    }

    public static <T> Criteria eq(String fieldName, T targetValue) {
        return new EqCriteria<T>(fieldName, targetValue);
    }

    public static Criteria and(Criteria leftCriteria, Criteria rightCriteria) {
        return new AndCriteria(leftCriteria, rightCriteria);
    }

    public static Criteria or(Criteria leftCriteria, Criteria rightCriteria) {
        return new OrCriteria(leftCriteria, rightCriteria);
    }

    public static <T> Criteria gt(String fieldName, T fieldValue) {
        return new GreaterThanCriteria<T>(fieldName, fieldValue);
    }

    public static <T> Criteria lt(String fieldName, T fieldValue) {
        return new LessThanCriteria<T>(fieldName, fieldValue);
    }
}
