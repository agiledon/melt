package com.melt.orm.session.criteria;

import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.criteria.OrCriteria;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OrCriteriaTest {
    @Test
    public void should_generate_or_clause() {
        OrCriteria criteria = new OrCriteria(new EqCriteria("id", 1), new EqCriteria("name", "ZhangYi"));
        assertThat(criteria.toExpression(), is("id = 1 OR name = 'ZhangYi'"));
    }
}
