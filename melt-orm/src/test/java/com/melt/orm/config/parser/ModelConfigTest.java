package com.melt.orm.config.parser;

//import com.melt.orm.dialect.MysqlDialect;
import org.junit.Before;
import org.junit.Test;
import sample.model.*;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelConfigTest {

    private ModelMappingHelper helper;
    private ModelConfig modelConfig;
    private Map<String, ModelConfig> modelConfigs;

    @Before
    public void setUp() throws Exception {
        helper = new ModelMappingHelper();
        modelConfig = helper.mappingClass2Model(Order.class);
        modelConfigs = of(
                Customer.class.getName(), helper.mappingClass2Model(Customer.class),
                Order.class.getName(), helper.mappingClass2Model(Order.class),
                Bill.class.getName(), helper.mappingClass2Model(Bill.class),
                Item.class.getName(), helper.mappingClass2Model(Item.class)
        );
    }

    @Test
    public void should_generate_create_table_sql() {
        assertThat(modelConfig.getPrimaryKeys().get(0).getFieldName(), is("id"));
//        System.out.println(modelConfig.generateCreateTableSQL(new MysqlDialect(), modelConfigs));
    }

    @Test
    public void should_return_table_name() {
        assertThat(modelConfig.getTableName(), is("ORDERS"));
    }

    @Test
    public void should_return_table_name_with_many_words() {
        modelConfig = helper.mappingClass2Model(OnlyOneField.class);
        assertThat(modelConfig.getTableName(), is("ONLY_ONE_FIELDS"));
    }
}
