package com.melt.orm.config.parser;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.melt.orm.exceptions.MeltOrmException;
import org.junit.Before;
import org.junit.Test;
import sample.model.OnlyOneField;
import sample.model.Order;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.FluentIterable.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class ModelMappingHandlerTest {

    private ModelMappingHandler helper;

    @Before
    public void setUp() throws Exception {
        helper = new ModelMappingHandler();
    }

    @Test
    public void should_return_all_model_configs_under_package(){
        Map<String,ModelConfig> modelConfigMap = helper.mappingModelConfigs("sample.model");
        assertThat(modelConfigMap.containsKey(Order.class.getName()), is(true));
        ModelConfig orderModelConfig = modelConfigMap.get(Order.class.getName());
        Optional<FieldConfig> billFieldConfig = from(orderModelConfig.getFields()).firstMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(FieldConfig input) {
                return input.getFieldName().endsWith("bill");
            }
        });
        if (billFieldConfig.isPresent()) {
            assertThat(billFieldConfig.get().isOneToOneField(), is(true));
        }else {
            fail();
        }

        Optional<FieldConfig> customerFieldConfig = from(orderModelConfig.getFields()).firstMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(FieldConfig input) {
                return input.getFieldName().endsWith("customer");
            }
        });
        if (customerFieldConfig.isPresent()) {
            assertThat(customerFieldConfig.get().isManyToOneField(), is(true));
        }else {
            fail();
        }

        Optional<FieldConfig> itemsFieldConfig = from(orderModelConfig.getFields()).firstMatch(new Predicate<FieldConfig>() {
            @Override
            public boolean apply(FieldConfig input) {
                return input.getFieldName().equals("items");
            }
        });
        if (itemsFieldConfig.isPresent()) {
            assertThat(itemsFieldConfig.get().isOneToManyField(), is(true));
        }else {
            fail();
        }
    }
}
