package com.melt.orm.config.parser;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FieldConfigTest {
    @Test
    public void should_return_true_when_filed_end_with_id(){
        FieldConfig config = new FieldConfig("orderId", Integer.TYPE);
        assertThat(config.isPrimaryKeyField(), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_exception_when_filed_name_is_null(){
        FieldConfig config = new FieldConfig(null, Integer.TYPE);
        assertThat(config.isPrimaryKeyField(), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_exception_when_filed_type_is_null(){
        FieldConfig config = new FieldConfig(null, null);
        assertThat(config.isPrimaryKeyField(), is(true));
    }

    @Test
    public void should_return_true_when_the_file_is_one_to_one() {
        FieldConfig config = new FieldConfig("orders", List.class);
        config.setManyToOne(true);
        assertThat(config.isNeedBeProxy(), is(true));
    }

    @Test
    public void should_return_column_name_with_underline_for_field_name() {
        FieldConfig config = new FieldConfig("orderName", String.class);
        assertThat(config.getColumnName(), is("ORDER_NAME"));
    }
}
