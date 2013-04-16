package com.melt.core;

import com.melt.exceptions.MoreThanOneBeanWithSameClass;
import com.melt.sample.bank.beans.DefaultBankDao;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BeansContainerTest {

    private BeansContainer container;

    @Before
    public void setUp() throws Exception {
        container = new BeansContainer();
    }

    @Test
    public void should_return_bean_when_get_bean_with_name(){
        Object bean = new Object();
        String beanName = "test";
        container.addBean(beanName, bean);
        assertThat(container.resolve(beanName), is(bean));
    }

    @Test
    public void should_return_bean_when_get_bean_with_class(){
        DefaultBankDao bankDao = new DefaultBankDao();
        Class<DefaultBankDao> clazz = DefaultBankDao.class;
        container.addBean(clazz, "bankDao", bankDao);
        assertThat((DefaultBankDao)container.resolve(clazz), is(bankDao));
    }

    @Test(expected = MoreThanOneBeanWithSameClass.class)
    public void should_throw_exception_when_more_than_one_beans_with_same_class(){
        DefaultBankDao bankDao1 = new DefaultBankDao();
        DefaultBankDao bankDao2 = new DefaultBankDao();
        Class<DefaultBankDao> clazz = DefaultBankDao.class;
        container.addBean(clazz, "bankDao1", bankDao1);
        container.addBean(clazz, "bankDao2", bankDao2);
        container.resolve(clazz);
    }

    @Test
    public void should_get_class_when_get_class_by_bean_name(){
        Class clazz = DefaultBankDao.class;
        container.addClass("bankDao", clazz);
        assertThat(container.getClazz("bankDao").equals(clazz), is(true));
    }
}
