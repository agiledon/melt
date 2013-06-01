package com.melt.orm.criteria;

import org.junit.Test;

import static com.melt.orm.criteria.By.and;
import static com.melt.orm.criteria.By.eq;
import static com.melt.orm.criteria.By.id;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AndCriteriaTest {
    @Test
    public void should_generate_and_clause() {
        Criteria andCriteria = and(id(1), eq("name", "ZhangYi"));
        assertThat(andCriteria.evaluate(), is("(ID = 1 AND NAME = 'ZhangYi')"));
    }
}
