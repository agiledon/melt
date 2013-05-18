package com.melt.orm.config.parser;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
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
}
