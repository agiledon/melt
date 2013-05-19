package com.melt.orm.config.parser;

import com.melt.orm.exceptions.MeltOrmException;
import org.junit.Before;
import org.junit.Test;
import sample.model.OnlyOneField;
import sample.model.Order;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ModelMappingHelperTest {

    private ModelMappingHelper helper;

    @Before
    public void setUp() throws Exception {
        helper = new ModelMappingHelper();
    }

    @Test(expected = MeltOrmException.class)
    public void should_throw_exception_when_package_wrong(){
        helper.getClassesUnderPackage("sample.model1");
    }

    @Test(expected = MeltOrmException.class)
    public void should_throw_exception_when_package_not_contain_a_model(){
        helper.getClassesUnderPackage("sample.no.model");
    }

    @Test
    public void should_return_all_filed_configs(){
        List<FieldConfig> fieldConfigs = helper.getFieldConfigs(OnlyOneField.class);
        FieldConfig config = fieldConfigs.get(0);
        assertThat(config.isPrimaryKeyField(), is(true));
        assertThat(config.getFieldName(), is("fieldId"));
        assertThat(config.getFieldType().getName(), is(Integer.TYPE.getName()));
    }

    @Test
    public void should_return_model_config_from_class() {
        ModelConfig modelConfig = helper.mappingClass2Model(Order.class);
        assertThat(modelConfig.getModelClass().getName(), is(Order.class.getName()));
        assertThat(modelConfig.getPrimaryKeys().get(0).getFieldName(), is("id"));
    }
}
