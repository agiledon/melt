package com.melt.orm.criteria;

import com.melt.orm.criteria.EqCriteria;
import org.junit.Test;

import static com.melt.orm.criteria.By.eq;
import static com.melt.orm.criteria.By.id;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EqCriteriaTest {
    @Test
    public void should_generate_to_equal_expression_with_name() {
        Criteria criteria = eq("name", "ZhangYi");
        assertThat(criteria.toExpression(), is("NAME = 'ZhangYi'"));
    }

    @Test
    public void should_generate_to_equal_expression_with_id() {
        Criteria criteria = id(1);
        assertThat(criteria.toExpression(), is("ID = 1"));
    }
}
