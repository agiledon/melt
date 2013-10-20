package com.github.agiledon.melt.config.constructor;

import com.github.agiledon.melt.config.BeanInfo;
import com.github.agiledon.melt.core.InjectionContext;
import com.github.agiledon.melt.core.InitializedBeans;
import com.github.agiledon.melt.sample.customer.dao.CustomerDao;
import com.github.agiledon.melt.sample.customer.domain.Customer;
import com.github.agiledon.melt.sample.customer.service.CustomerFiller;
import com.github.agiledon.melt.sample.customer.service.DefaultCustomerService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class ConstructorTest {
    private BeanInfo customerServiceBean;
    private InitializedBeans container;

    @Before
    public void setUp() throws Exception {
        container = new InitializedBeans();
    }

    @Test
    public void should_inject_CustomerDao_with_constructor_of_CustomerService() {
        customerServiceBean = new BeanInfo("customerService", DefaultCustomerService.class);
        customerServiceBean.addConstructorParameter(new RefConstructorParameter(0, "customerDao"));
        container.addBean("customerDao", new CustomerDao());

        customerServiceBean.getConstructor().initialize(new InjectionContext(null, container));

        DefaultCustomerService customerService = (DefaultCustomerService) container.getBean("customerService");

        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(0));
        assertThat(customers.get(0).getName(), is(nullValue()));
    }

    @Test
    public void should_inject_CustomerDao_And_CustomerFiller_with_constructor_of_CustomerService() {
        customerServiceBean = new BeanInfo("customerService", DefaultCustomerService.class);
        customerServiceBean.addConstructorParameter(new RefConstructorParameter(0, "customerDao"));
        customerServiceBean.addConstructorParameter(new RefConstructorParameter(1, "customerFiller"));

        container.addBean("customerDao", new CustomerDao());
        container.addBean("customerFiller", new CustomerFiller());

        customerServiceBean.getConstructor().initialize(new InjectionContext(null, container));

        DefaultCustomerService customerService = (DefaultCustomerService) container.getBean("customerService");

        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }
}
