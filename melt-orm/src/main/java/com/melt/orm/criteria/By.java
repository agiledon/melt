package com.melt.orm.criteria;

public class By {
    public static Criteria id(int id) {
        return new EqCriteria("id", id);
    }

    public static Criteria eq(String fieldName, String targetValue) {
        return new EqCriteria(fieldName, targetValue);
    }
}
