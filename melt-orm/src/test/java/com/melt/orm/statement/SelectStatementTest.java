package com.melt.orm.statement;

import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Test;
import sample.model.Customer;

import static com.melt.orm.criteria.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SelectStatementTest {

    private Session session;
    private SelectStatement statement;

    @Before
    public void setUp() throws Exception {
        session = TestFixture.prepareSession();
        statement = new SelectStatement(session);
    }

    @Test
    public void should_parse_to_select_statement() {
        statement.assemble(Customer.class);
        assertThat(statement.getSql(), is("SELECT ID, NAME, AGE, CUSTOMER_TYPE FROM CUSTOMERS"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id() {
        statement.assemble(Customer.class, id(1));
        assertThat(statement.getSql(), is("SELECT ID, NAME, AGE, CUSTOMER_TYPE FROM CUSTOMERS WHERE ID = 1"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id_and_name() {
        statement.assemble(
                Customer.class,
                and(eq("id", 1), eq("name", "ZhangYi")));
        assertThat(statement.getSql(), is("SELECT ID, NAME, AGE, CUSTOMER_TYPE FROM CUSTOMERS WHERE (ID = 1 AND NAME = 'ZhangYi')"));
    }
}
