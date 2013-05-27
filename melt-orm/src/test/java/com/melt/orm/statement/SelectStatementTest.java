package com.melt.orm.statement;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.session.Session;
import com.melt.orm.criteria.AndCriteria;
import org.junit.Before;
import org.junit.Test;
import sample.model.Customer;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.melt.orm.criteria.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectStatementTest {

    private Session session;
    private SelectStatement statementParser;

    @Before
    public void setUp() throws Exception {
        session = prepareSession();
        statementParser = new SelectStatement(session);
    }

    @Test
    public void should_parse_to_select_statement() {
        SqlStatement sqlStatement = statementParser.assemble(Customer.class, nil());
        assertThat(sqlStatement.getSql(), is("SELECT ID, NAME FROM CUSTOMERS"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id() {
        SqlStatement sqlStatement = statementParser.assemble(Customer.class, id(1));
        assertThat(sqlStatement.getSql(), is("SELECT ID, NAME FROM CUSTOMERS WHERE ID = 1"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id_and_name() {
        SqlStatement sqlStatement = statementParser.assemble(
                Customer.class,
                and(eq("id", 1), eq("name", "ZhangYi")));
        assertThat(sqlStatement.getSql(), is("SELECT ID, NAME FROM CUSTOMERS WHERE (ID = 1 AND NAME = 'ZhangYi')"));
    }


    private Session prepareSession() {
        Session session = mock(Session.class);
        Map<String, ModelConfig> modelConfigs = newHashMap();
        List<FieldConfig> fieldConfigs = newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("name", String.class));
        FieldConfig orders = new FieldConfig("orders", List.class);
        orders.setOneToMany(true);
        fieldConfigs.add(orders);
        modelConfigs.put(Customer.class.getName(), new ModelConfig(fieldConfigs, Customer.class));
        when(session.getModelConfigs()).thenReturn(modelConfigs);
        return session;
    }
}
