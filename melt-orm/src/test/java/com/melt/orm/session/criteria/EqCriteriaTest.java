package com.melt.orm.session.criteria;

import com.melt.orm.criteria.EqCriteria;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EqCriteriaTest {
    @Test
    public void should_generate_to_equal_expression_with_name() {
        EqCriteria criteria = new EqCriteria("name", "ZhangYi");
        assertThat(criteria.toExpression(), is("name = 'ZhangYi'"));
    }

    @Test
    public void should_generate_to_equal_expression_with_id() {
        EqCriteria criteria = new EqCriteria("id", 1);
        assertThat(criteria.toExpression(), is("id = 1"));
    }
}
