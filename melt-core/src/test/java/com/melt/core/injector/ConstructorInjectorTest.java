package com.melt.core.injector;

import com.melt.config.ConstructorFields;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.domain.Customer;
import com.melt.sample.service.CustomerFiller;
import com.melt.sample.service.CustomerService;
import com.melt.sample.service.DefaultCustomerService;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ConstructorInjectorTest {
    @Test
    public void should_inject_CustomerDao_with_constructor_of_CustomerService() {
        ConstructorInjector injector = new ConstructorInjector();

        CustomerService customerService = injector.inject(DefaultCustomerService.class, new CustomerDao());

        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
    }

    @Test
    public void should_inject_CustomerDao_And_CustomerFiller_with_constructor_of_CustomerService() {
        ConstructorInjector injector = new ConstructorInjector();

        CustomerService customerService = injector.inject(DefaultCustomerService.class, new CustomerDao(), new CustomerFiller());

        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }

}
