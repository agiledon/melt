package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.RefConstructorParameter;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultConstructorInitializerTest {

    private DefaultConstructorInitializer initializer;

    @Before
    public void setUp() throws Exception {
        initializer = new DefaultConstructorInitializer();
    }

    @Test
    public void should_return_default_bankDao_bean() {
        Object bean = initializer.initialize(new BeanInfo("bankDao", DefaultBankDao.class));
        assertThat(bean, instanceOf(BankDao.class));
    }

    @Test
    public void should_throw_exception_when_bean_config_has_constructor_config() {
        BeanInfo bankDao = new BeanInfo("bankDao", DefaultBankDao.class);
        bankDao.addConstructorParameter(new RefConstructorParameter(0, "ref"));
        Object initialize = initializer.initialize(bankDao);
        assertThat(initialize, is(nullValue()));
    }
}
