package com.melt.orm.session.criteria;

import com.melt.orm.criteria.AndCriteria;
import com.melt.orm.criteria.BinaryCriteria;
import com.melt.orm.criteria.EqCriteria;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AndCriteriaTest {
    @Test
    public void should_generate_and_clause() {
        BinaryCriteria andCriteria = new AndCriteria(new EqCriteria("id", 1), new EqCriteria("name", "ZhangYi"));
        assertThat(andCriteria.toExpression(), is("id == 1 and name == 'ZhangYi'"));
    }
}
