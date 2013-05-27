package com.melt.orm.criteria;

import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.criteria.OrCriteria;
import org.junit.Test;

import static com.melt.orm.criteria.By.eq;
import static com.melt.orm.criteria.By.id;
import static com.melt.orm.criteria.By.or;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OrCriteriaTest {
    @Test
    public void should_generate_or_clause() {
        Criteria criteria = or(id(1), eq("name", "ZhangYi"));
        assertThat(criteria.toExpression(), is("(ID = 1 OR NAME = 'ZhangYi')"));
    }
}
