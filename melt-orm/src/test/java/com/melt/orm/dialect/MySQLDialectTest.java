package com.melt.orm.dialect;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MySQLDialectTest {
    @Test
    public void should_return_true_when_type_is_basic(){
        MySQLDialect dialect = new MySQLDialect();
        assertThat(dialect.isBasicType(Integer.class), is(true));
        assertThat(dialect.isBasicType(Integer.TYPE), is(true));
        assertThat(dialect.mappingFieldType(Integer.class), is("TINYINT UNSIGNED"));
    }

    @Test
    public void should_return_false_when_type_is_not_basic(){
        MySQLDialect dialect = new MySQLDialect();
        assertThat(dialect.isBasicType(List.class), is(false));
    }

}
