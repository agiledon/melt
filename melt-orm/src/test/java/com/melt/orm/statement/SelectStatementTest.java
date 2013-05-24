package com.melt.orm.statement;

import com.melt.orm.config.parser.FieldConfig;
import com.melt.orm.config.parser.ModelConfig;
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
        SqlStatement sqlStatement = statementParser.assemble(new Customer(), new NullCriteria());
        assertThat(sqlStatement.getSql().toLowerCase(), is("select id, name from customers"));
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id() {
        SqlStatement sqlStatement = statementParser.assemble(new Customer(), createEqCriteria("id", 1));
        assertThat(sqlStatement.getSql().toLowerCase(), is("select id, name from customers where id == 1"));
    }

    private <T> EqCriteria createEqCriteria(String fieldName, T fieldValue) {
        return new EqCriteria(fieldName, fieldValue);
    }

    @Test
    public void should_parse_to_select_statement_with_condition_clause_by_id_and_name() {
        SqlStatement sqlStatement = statementParser.assemble(
                new Customer(),
                new AndCriteria(createEqCriteria("id", 1), createEqCriteria("name", "ZhangYi")));
        assertThat(sqlStatement.getSql().toLowerCase(), is("select id, name from customers where id == 1 and name == 'ZhangYi'".toLowerCase()));
    }

    private Session prepareSession() {
        Session session = mock(Session.class);
        Map<String, ModelConfig> modelConfigs = newHashMap();
        List<FieldConfig> fieldConfigs = newArrayList();
        fieldConfigs.add(new FieldConfig("id", Integer.class));
        fieldConfigs.add(new FieldConfig("name", String.class));
        modelConfigs.put(Customer.class.getName(), new ModelConfig(fieldConfigs, Customer.class));

        when(session.getModelConfigs()).thenReturn(modelConfigs);
        return session;
    }
}
