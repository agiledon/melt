package com.melt.orm.criteria;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SingleCriteriaTest {
    @Test
    public void should_generate_expression_with_greater_than_int_value(){
        GreaterThanCriteria criteria = new GreaterThanCriteria("age", 30);
        assertThat(criteria.toExpression(), is("age > 30"));
    }

    @Test
    public void should_generate_expression_with_greater_than_double_value(){
        GreaterThanCriteria criteria = new GreaterThanCriteria("salary", 3000.0);
        assertThat(criteria.toExpression(), is("salary > 3000.0"));
    }

    @Test
    public void should_generate_expression_with_less_than_int_value(){
        LessThanCriteria criteria = new LessThanCriteria("age", 30);
        assertThat(criteria.toExpression(), is("age < 30"));
    }

    @Test
    public void should_generate_expression_with_less_than_double_value(){
        LessThanCriteria criteria = new LessThanCriteria("salary", 3000.0);
        assertThat(criteria.toExpression(), is("salary < 3000.0"));
    }
}
