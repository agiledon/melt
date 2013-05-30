package com.melt.orm.statement;

import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Test;
import sample.model.Customer;

import static com.melt.orm.criteria.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DeleteStatementTest {
    private Session session;
    private DeleteStatement statement;

    @Before
    public void setUp() throws Exception {
        session = TestFixture.prepareSession();
        statement = new DeleteStatement(session);
    }

    @Test
    public void should_parse_to_delete_statement() {
        statement.assemble(Customer.class);
        assertThat(statement.getSql(), is("DELETE FROM CUSTOMERS"));
    }

    @Test
    public void should_parse_to_delete_statement_with_condition_clause() {
        statement.assemble(Customer.class, and(id(1), eq("name", "ZhangYi")));
        assertThat(statement.getSql(), is("DELETE FROM CUSTOMERS WHERE (ID = 1 AND NAME = 'ZhangYi')"));
    }
}
