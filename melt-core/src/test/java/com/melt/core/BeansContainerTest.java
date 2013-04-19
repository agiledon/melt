package com.melt.core;

import com.melt.exceptions.MoreThanOneBeanWithSameClass;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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
        container.addBean("bankDao", clazz, bankDao);
        assertThat((DefaultBankDao)container.resolve(DefaultBankDao.class), is(bankDao));
    }

    @Test
    public void should_return_bean_when_add_bean_with_many_class(){
        DefaultBankDao bankDao = new DefaultBankDao();
        Class<DefaultBankDao> clazz = DefaultBankDao.class;
        List<Class> classes = newArrayList(new Class[]{DefaultBankDao.class, BankDao.class});
        container.addBean(classes, "bankDao", bankDao);
        assertThat((DefaultBankDao) container.resolve(DefaultBankDao.class), is(bankDao));
    }

    @Test(expected = MoreThanOneBeanWithSameClass.class)
    public void should_throw_exception_when_more_than_one_beans_with_same_class(){
        DefaultBankDao bankDao1 = new DefaultBankDao();
        DefaultBankDao bankDao2 = new DefaultBankDao();
        Class<DefaultBankDao> clazz = DefaultBankDao.class;
        container.addBean("bankDao1", clazz, bankDao1);
        container.addBean("bankDao2", clazz, bankDao2);
        DefaultBankDao dao = container.resolve(DefaultBankDao.class);
    }


}
