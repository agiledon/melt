package com.github.agiledon.melt.core;

import com.github.agiledon.melt.exceptions.MoreThanOneBeanWithSameClass;
import com.github.agiledon.melt.sample.beans.BankDao;
import com.github.agiledon.melt.sample.beans.DefaultBankDao;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InitializedBeansTest {

    private InitializedBeans container;

    @Before
    public void setUp() throws Exception {
        container = new InitializedBeans();
    }

    @Test
    public void should_return_bean_when_get_bean_with_name(){
        Object bean = new Object();
        String beanName = "test";
        container.addBean(beanName, bean);
        assertThat(container.getBean(beanName), is(bean));
    }

    @Test
    public void should_return_bean_when_get_bean_with_class(){
        DefaultBankDao bankDao = new DefaultBankDao();
        Class<DefaultBankDao> clazz = DefaultBankDao.class;
        container.addBean("bankDao", clazz, bankDao);
        assertThat((DefaultBankDao)container.getBean(DefaultBankDao.class), is(bankDao));
    }

    @Test
    public void should_return_bean_when_add_bean_with_many_class(){
        DefaultBankDao bankDao = new DefaultBankDao();
        List<Class> classes = newArrayList(new Class[]{DefaultBankDao.class, BankDao.class});
        container.addBean("bankDao", classes, bankDao);
        assertThat((DefaultBankDao) container.getBean(DefaultBankDao.class), is(bankDao));
    }

    @Test(expected = MoreThanOneBeanWithSameClass.class)
    public void should_throw_exception_when_more_than_one_beans_with_same_class(){
        DefaultBankDao bankDao1 = new DefaultBankDao();
        DefaultBankDao bankDao2 = new DefaultBankDao();
        Class<DefaultBankDao> clazz = DefaultBankDao.class;
        container.addBean("bankDao1", clazz, bankDao1);
        container.addBean("bankDao2", clazz, bankDao2);
        DefaultBankDao dao = container.getBean(DefaultBankDao.class);
    }


}
