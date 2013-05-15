package com.melt.core;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.RefConstructorParameter;
import com.melt.config.property.BeanRefProperty;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import com.melt.sample.customer.dao.CustomerDao;
import com.melt.sample.customer.domain.Customer;
import com.melt.sample.customer.service.CustomerFiller;
import com.melt.sample.customer.service.DefaultCustomerService;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class BeansInitializerTest {

    private BeansInitializer initializer;

    @Before
    public void setUp() throws Exception {
        initializer = new BeansInitializer();
    }

    @Test
    public void should_initialize_DefaultBankService_object_with_one_property() {
        BeansInitializer initializer = new BeansInitializer();

        BeanInfo bankService = new BeanInfo("bankService", DefaultBankService.class);
        bankService.addProperty(new BeanRefProperty(bankService, "bankDao", "bankDao"));
        BeanInfo bankDao = new BeanInfo("bankDao", DefaultBankDao.class);

        InitializedBeans initializedBeans = initializer.initialize(null, newArrayList(bankService, bankDao));
        DefaultBankService bankServiceBean = (DefaultBankService) initializedBeans.getBean("bankService");
        assertThat(bankServiceBean, instanceOf(DefaultBankService.class));
        assertThat(bankServiceBean.getBankDao(), not(nullValue()));
        assertThat(bankServiceBean.getBankDao(), instanceOf(DefaultBankDao.class));
    }

    @Test
    public void should_initialize_DefaultCustomerService_object_with_one_constructor_parameter() {
        BeanInfo customerService = new BeanInfo("customerService", DefaultCustomerService.class);
        customerService.addConstructorParameter(new RefConstructorParameter(0, "customerDao"));
        BeanInfo customerDao = new BeanInfo("customerDao", CustomerDao.class);

        InitializedBeans initializedBeans = initializer.initialize(null, newArrayList(customerService, customerDao));
        DefaultCustomerService customerServiceBean = (DefaultCustomerService) initializedBeans.getBean("customerService");
        assertThat(customerServiceBean, instanceOf(DefaultCustomerService.class));
        assertThat(customerServiceBean.allCustomers().size(), is(1));

    }

    @Test
    public void should_initialize_DefaultCustomerService_object_with_two_constructor_parameters() {
        BeanInfo customerService = new BeanInfo("customerService", DefaultCustomerService.class);
        customerService.addConstructorParameter(new RefConstructorParameter(0, "customerDao"));
        customerService.addConstructorParameter(new RefConstructorParameter(1, "customerFiller"));
        BeanInfo customerDao = new BeanInfo("customerDao", CustomerDao.class);
        BeanInfo customerFiller = new BeanInfo("customerFiller", CustomerFiller.class);

        InitializedBeans initializedBeans = initializer.initialize(null, newArrayList(customerService, customerDao, customerFiller));
        DefaultCustomerService customerServiceBean = (DefaultCustomerService) initializedBeans.getBean("customerService");
        assertThat(customerServiceBean, instanceOf(DefaultCustomerService.class));
        List<Customer> customers = customerServiceBean.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), Is.is(1));
        assertThat(customers.get(0).getName(), Is.is("zhangyi"));
    }
}
