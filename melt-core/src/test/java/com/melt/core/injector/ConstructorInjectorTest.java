package com.melt.core.injector;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.constructor.ConstructorParameters;
import com.melt.core.BeansContainer;
import com.melt.sample.customer.dao.CustomerDao;
import com.melt.sample.customer.domain.Customer;
import com.melt.sample.customer.service.CustomerFiller;
import com.melt.sample.customer.service.CustomerService;
import com.melt.sample.customer.service.DefaultCustomerService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class ConstructorInjectorTest {

    private ConstructorInjector constructorInjector;
    private BeanInfo customerServiceBean;
    private BeansContainer container;

    @Before
    public void setUp() throws Exception {
        constructorInjector = new ConstructorInjector();
        container = new BeansContainer();
    }

    @Test
    public void should_inject_CustomerDao_with_constructor_of_CustomerService() {
        List<ConstructorParameter> parameters = newArrayList();
        parameters.add(new ConstructorParameter(0, "CustomerDao"));
        ConstructorParameters constructorParameters = new ConstructorParameters(parameters);

        customerServiceBean = new BeanInfo(DefaultCustomerService.class, constructorParameters);
        container.addBean("CustomerDao", new CustomerDao());

        constructorInjector.inject(container, customerServiceBean);

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);

        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(0));
        assertThat(customers.get(0).getName(), is(nullValue()));
    }

    @Test
    public void should_inject_CustomerDao_And_CustomerFiller_with_constructor_of_CustomerService() {
        List<ConstructorParameter> parameters = newArrayList();
        parameters.add(new ConstructorParameter(0, "CustomerDao"));
        parameters.add(new ConstructorParameter(1, "CustomerFiller"));
        ConstructorParameters constructorParameters = new ConstructorParameters(parameters);

        customerServiceBean = new BeanInfo(DefaultCustomerService.class, constructorParameters);
        container.addBean("CustomerDao", new CustomerDao());
        container.addBean("CustomerFiller", new CustomerFiller());

        constructorInjector.inject(container, customerServiceBean);

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);

        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }
}
