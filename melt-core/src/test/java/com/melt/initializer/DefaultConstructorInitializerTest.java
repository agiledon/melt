package com.melt.initializer;

import com.melt.beans.BankDao;
import com.melt.beans.DefaultBankDao;
import com.melt.config.BeanInfo;
import com.melt.config.ConstructorFields;
import com.melt.exceptions.InitBeanException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class DefaultConstructorInitializerTest {

    @Test
    public void should_return_default_bankDao_bean() {
        DefaultConstructorInitializer initializer = new DefaultConstructorInitializer();
        Object bean = initializer.initialize(new BeanInfo("bankDao", DefaultBankDao.class.getName()));
        assertThat(bean, instanceOf(BankDao.class));
    }

    @Test(expected = InitBeanException.class)
    public void should_throw_exception_when_bean_config_has_constructor_config() {
        DefaultConstructorInitializer initializer = new DefaultConstructorInitializer();
        Object bean = initializer.initialize(new BeanInfo("bankDao", new ConstructorFields(null)));
    }

    @Test(expected = InitBeanException.class)
    public void should_throw_exception_when_class_not_exist() {
        DefaultConstructorInitializer initializer = new DefaultConstructorInitializer();
        Object bean = initializer.initialize(new BeanInfo("bankDao", "notExistClass"));
    }


}
