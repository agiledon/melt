package com.melt.orm.statement;

import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import sample.model.Customer;
import sample.model.Order;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.melt.orm.criteria.By.nil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.internal.util.collections.Sets.newSet;

public class UpdateStatementTest {
    private Session session;
    private UpdateStatement statement;

    @Before
    public void setUp() throws Exception {
        session = SessionFixture.prepareSession();
        statement = new UpdateStatement(session);
    }

    @Test
    @Ignore
    public void should_parse_to_update_clause() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("ZhangYi");
        Set<Order> orders = newSet();
        customer.setOrders(orders);
        statement.assemble(customer, nil());
        assertThat(statement.getSql(), is("UPDATE CUSTOMERS SET NAME = 'ZhangYi'"));
    }
}
