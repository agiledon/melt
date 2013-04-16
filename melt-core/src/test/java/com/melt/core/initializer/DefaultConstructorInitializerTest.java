package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameters;
import com.melt.exceptions.InitBeanException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class DefaultConstructorInitializerTest {

    private DefaultConstructorInitializer initializer;

    @Before
    public void setUp() throws Exception {
        initializer = new DefaultConstructorInitializer();
    }

    @Test
    public void should_return_default_bankDao_bean() {
//        Object bean = initializer.initialize(new BeanInfo("bankDao", DefaultBankDao.class.getName()));
//        assertThat(bean, instanceOf(BankDao.class));
    }

    @Test(expected = InitBeanException.class)
    public void should_throw_exception_when_bean_config_has_constructor_config() {
        initializer.initialize(new BeanInfo("bankDao", new ConstructorParameters(null)));
    }

    @Test(expected = InitBeanException.class)
    public void should_throw_exception_when_class_not_exist() {
        initializer.initialize(new BeanInfo("bankDao", "notExistClass"));
    }
}
