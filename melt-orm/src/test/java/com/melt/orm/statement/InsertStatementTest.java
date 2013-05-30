package com.melt.orm.statement;

import com.melt.orm.session.Session;
import org.junit.Before;
import org.junit.Test;
import sample.model.Customer;
import sample.model.CustomerType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class InsertStatementTest {
    private Session session;
    private InsertStatement statement;

    @Before
    public void setUp() throws Exception {
        session = TestFixture.prepareSession();
        statement = new InsertStatement(session);
    }

    @Test
    public void should_parse_to_insert_statement() {
        Customer customer = new Customer();
        customer.setId(2);
        customer.setName("ZhangYi");
        customer.setAge(37);
        customer.setCustomerType(CustomerType.COMMONS);
        statement.assemble(customer);
        System.out.println(statement.getSql());
        assertThat(statement.getSql(), is("INSERT INTO CUSTOMERS (NAME, AGE, CUSTOMER_TYPE) VALUES ('ZhangYi', 37, 'COMMONS')"));
    }
}
