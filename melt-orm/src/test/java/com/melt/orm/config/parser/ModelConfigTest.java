package com.melt.orm.config.parser;

import com.melt.orm.dialect.MySQLDialect;
import org.junit.Before;
import org.junit.Test;
import sample.model.*;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelConfigTest {

    private ModelMappingHandler helper;
    private ModelConfig modelConfig;
    private Map<String, ModelConfig> modelConfigs;

    @Before
    public void setUp() throws Exception {
        helper = new ModelMappingHandler();
        modelConfigs = helper.mappingModelConfigs("sample.model");
        modelConfig = modelConfigs.get(Order.class.getName());
    }

    @Test
    public void should_generate_create_table_sql() {
        assertThat(modelConfig.getPrimaryKey().get().getFieldName(), is("id"));
        System.out.println(modelConfig.generateDropTableSQL());
        System.out.println(modelConfig.generateCreateTableSQL(new MySQLDialect(), modelConfigs));
    }

    @Test
    public void should_return_table_name() {
        assertThat(modelConfig.getTableName(), is("ORDERS"));
    }
}
