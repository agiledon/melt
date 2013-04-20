package com.melt.core;

import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import com.melt.sample.customer.dao.CustomerDao;
import com.melt.sample.customer.dao.JdbcTemplate;
import com.melt.sample.customer.domain.Customer;
import com.melt.sample.customer.service.CustomerFiller;
import com.melt.sample.customer.service.DefaultCustomerService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ContextBuilderTest {

    private ContextBuilder builder;
    private Context context;

    @Before
    public void setUp() throws Exception {
        builder = new ContextBuilder();
    }

    @Test
    public void should_register_CustomerService_bean() {
        context = builder.register(DefaultCustomerService.class)
                .build();

        assertThat(context.resolve(DefaultCustomerService.class), instanceOf(DefaultCustomerService.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_two_constructor_parameters() {
        context = builder.register(DefaultCustomerService.class)
                .construct(CustomerDao.class)
                .construct(CustomerFiller.class)
                .build();

        DefaultCustomerService customerService = context.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }

    @Test
    public void should_register_DefaultBankService_bean_with_one_property() {
        context = builder.register(DefaultBankService.class)
                .withClass(DefaultBankDao.class)
                .build();

        DefaultBankService bankService = context.resolve(DefaultBankService.class);
        assertThat(bankService, not(nullValue()));
        assertThat(bankService.getBankDao(), instanceOf(DefaultBankDao.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_nested_properties() {
        context = builder.register(CustomerDao.class)
                .withClass(JdbcTemplate.class)
                .register(DefaultCustomerService.class)
                .withClass(CustomerDao.class)
                .withClass(CustomerFiller.class)
                .build();

        DefaultCustomerService customerService = context.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        CustomerDao customerDao = customerService.getCustomerDao();
        assertThat(customerDao, instanceOf(CustomerDao.class));
        assertThat(customerDao.getJdbcTemplate(), instanceOf(JdbcTemplate.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_two_properties_and_one_int_value_properties() {
        context = builder.register(DefaultCustomerService.class)
                .withClass(CustomerDao.class)
                .withClass(CustomerFiller.class)
                .withValue("count", 3)
                .build();

        DefaultCustomerService customerService = context.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getCount(), is(3));

    }

    @Test
    public void should_register_CustomerService_bean_with_two_properties_and_one_string_value_properties() {
        context = builder.register(DefaultCustomerService.class)
                .withClass(CustomerDao.class)
                .withClass(CustomerFiller.class)
                .withValue("message", "hello world")
                .build();

        DefaultCustomerService customerService = context.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getMessage(), is("hello world"));

    }
}
