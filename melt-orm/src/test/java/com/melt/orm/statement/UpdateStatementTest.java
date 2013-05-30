package com.melt.orm.statement;

import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Test;
import sample.model.Customer;
import sample.model.Order;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.melt.orm.criteria.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.internal.util.collections.Sets.newSet;

public class UpdateStatementTest {
    private Session session;
    private UpdateStatement statement;

    @Before
    public void setUp() throws Exception {
        session = TestFixture.prepareSession();
        statement = new UpdateStatement(session);
    }

    @Test
    public void should_parse_to_update_clause_with_name_and_age() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("ZhangYi");
        customer.setAge(37);
        Set<Order> orders = newSet();
        customer.setOrders(orders);
        statement.assemble(customer);
        assertThat(statement.getSql(), is("UPDATE CUSTOMERS SET NAME = 'ZhangYi', AGE = 37"));
    }

    @Test
    public void should_parse_to_update_clause_with_name_and_null_age() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("ZhangYi");
        Set<Order> orders = newSet();
        customer.setOrders(orders);
        statement.assemble(customer);
        assertThat(statement.getSql(), is("UPDATE CUSTOMERS SET NAME = 'ZhangYi', AGE = 0"));
    }

    @Test
    public void should_parse_to_update_clause_with_null_name_and_null_age() {
        Customer customer = new Customer();
        customer.setId(1);
        Set<Order> orders = newSet();
        customer.setOrders(orders);
        statement.assemble(customer);
        assertThat(statement.getSql(), is("UPDATE CUSTOMERS SET NAME = null, AGE = 0"));
    }

    @Test
    public void should_parse_to_update_clause_with_condition_clause() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("ZhangYi");
        Set<Order> orders = newSet();
        customer.setOrders(orders);
        statement.assemble(customer, and(id(1), gt("age", 40)));
        assertThat(statement.getSql(), is("UPDATE CUSTOMERS SET NAME = 'ZhangYi', AGE = 0 WHERE (ID = 1 AND AGE > 40)"));
    }
}
