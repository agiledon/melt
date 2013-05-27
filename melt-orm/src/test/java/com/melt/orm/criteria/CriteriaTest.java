package com.melt.orm.criteria;

import org.junit.Test;

import static com.melt.orm.criteria.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class CriteriaTest {
    @Test
    public void should_generate_composite_expression() {
        Criteria criteria = and(or(id(1), eq("age", 30)), and(gt("salary", 3000.0), lt("workInYear", 3)));
        assertThat(criteria.toExpression(), is("((ID = 1 OR AGE = 30) AND (SALARY > 3000.0 AND WORK_IN_YEAR < 3))"));
    }
}
