package com.melt.core.initializer;

import com.melt.config.BeanInfo;
import com.melt.config.constructor.ConstructorParameter;
import com.melt.config.property.BeanProperty;
import com.melt.config.property.BeanRefProperty;
import com.melt.core.BeansContainer;
import com.melt.sample.bank.beans.BankService;
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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContainerInitializerTest {

    private ContainerInitializer initializer;

    @Before
    public void setUp() throws Exception {
        initializer = new ContainerInitializer();
    }

    @Test
    public void should_initialize_DefaultBankService_object_with_one_property() {
        ContainerInitializer initializer = new ContainerInitializer();

        BeanInfo bankService = new BeanInfo("bankService", DefaultBankService.class);
        bankService.addProperty(new BeanRefProperty(bankService, "bankDao", "bankDao"));

        BeansContainer beansContainer = initializer.initialize(newArrayList(bankService));
        assertThat(beansContainer.resolve("bankService"), instanceOf(DefaultBankService.class));
    }

    @Test
    public void should_initialize_DefaultCustomerService_object_with_one_constructor_parameter() {
        BeanInfo customerService = new BeanInfo("customerService", DefaultCustomerService.class);
        customerService.addConstructorParameter(new ConstructorParameter(0, "customerDao"));
        BeanInfo customerDao = new BeanInfo("customerDao", CustomerDao.class);

        BeansContainer beansContainer = initializer.initialize(newArrayList(customerService, customerDao));
        DefaultCustomerService customerServiceBean = (DefaultCustomerService) beansContainer.resolve("customerService");
        assertThat(customerServiceBean, instanceOf(DefaultCustomerService.class));
        assertThat(customerServiceBean.allCustomers().size(), is(1));

    }

    @Test
    public void should_initialize_DefaultCustomerService_object_with_two_constructor_parameters() {
        BeanInfo customerService = new BeanInfo("customerService", DefaultCustomerService.class);
        customerService.addConstructorParameter(new ConstructorParameter(0, "customerDao"));
        customerService.addConstructorParameter(new ConstructorParameter(1, "customerFiller"));
        BeanInfo customerDao = new BeanInfo("customerDao", CustomerDao.class);
        BeanInfo customerFiller = new BeanInfo("customerFiller", CustomerFiller.class);

        BeansContainer beansContainer = initializer.initialize(newArrayList(customerService, customerDao, customerFiller));
        DefaultCustomerService customerServiceBean = (DefaultCustomerService) beansContainer.resolve("customerService");
        assertThat(customerServiceBean, instanceOf(DefaultCustomerService.class));
        List<Customer> customers = customerServiceBean.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), Is.is(1));
        assertThat(customers.get(0).getName(), Is.is("zhangyi"));
    }
}
