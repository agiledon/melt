package com.melt.orm.statement;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
import com.melt.orm.config.parser.ModelMappingHandler;
import com.melt.orm.session.Session;
import com.melt.orm.criteria.AndCriteria;
import com.melt.orm.criteria.EqCriteria;
import com.melt.orm.criteria.NullCriteria;
import org.junit.Before;
import org.junit.Test;
import sample.model.Customer;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
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
        SqlStatement sqlStatement = statementParser.assemble(Customer.class, new NullCriteria());
        assertThat(sqlStatement.getSql(), is("SELECT ID, NAME FROM CUSTOMERS"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id() {
        SqlStatement sqlStatement = statementParser.assemble(Customer.class, createEqCriteria("id", 1));
        assertThat(sqlStatement.getSql(), is("SELECT ID, NAME FROM CUSTOMERS WHERE id = 1"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id_and_name() {
        SqlStatement sqlStatement = statementParser.assemble(
                Customer.class,
                new AndCriteria(createEqCriteria("id", 1), createEqCriteria("name", "ZhangYi")));
        assertThat(sqlStatement.getSql(), is("SELECT ID, NAME FROM CUSTOMERS WHERE id = 1 AND name = 'ZhangYi'"));
    }

    private <T> EqCriteria createEqCriteria(String fieldName, T fieldValue) {
        return new EqCriteria(fieldName, fieldValue);
    }


    private Session prepareSession() {
        Session session = mock(Session.class);
        ModelMappingHandler helper = new ModelMappingHandler();
        Map<String,ModelConfig> modelConfigs = helper.mappingModelConfigs("sample.model");
        when(session.getModelConfigs()).thenReturn(modelConfigs);
        return session;
    }
}
