package com.melt.orm.criteria;

import org.junit.Test;

import static com.melt.orm.criteria.By.gt;
import static com.melt.orm.criteria.By.lt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SingleCriteriaTest {
    @Test
    public void should_generate_expression_with_greater_than_int_value(){
        Criteria criteria = gt("age", 30);
        assertThat(criteria.evaluate(), is("AGE > 30"));
    }

    @Test
    public void should_generate_expression_with_greater_than_double_value(){
        Criteria criteria = gt("salary", 3000.0);
        assertThat(criteria.evaluate(), is("SALARY > 3000.0"));
    }

    @Test
    public void should_generate_expression_with_less_than_int_value(){
        Criteria criteria = lt("age", 30);
        assertThat(criteria.evaluate(), is("AGE < 30"));
    }

    @Test
    public void should_generate_expression_with_less_than_double_value(){
        Criteria criteria = lt("salary", 3000.0);
        assertThat(criteria.evaluate(), is("SALARY < 3000.0"));
    }
}
