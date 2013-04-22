package com.melt.core;

import com.melt.bean.AutoWiredBy;
import com.melt.exceptions.BeanConfigurationException;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.BankService;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import com.melt.sample.customer.dao.AnotherCustomerDaoInterface;
import com.melt.sample.customer.dao.CustomerDao;
import com.melt.sample.customer.dao.CustomerDaoInterface;
import com.melt.sample.customer.dao.JdbcTemplate;
import com.melt.sample.customer.domain.Customer;
import com.melt.sample.customer.service.CustomerFiller;
import com.melt.sample.customer.service.DefaultCustomerService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ContainerBuilderTest {

    private ContainerBuilder builder;
    private Container container;

    @Before
    public void setUp() throws Exception {
        builder = new ContainerBuilder();
    }

    @Test
    public void should_register_CustomerService_bean() {
        container = builder.register(DefaultCustomerService.class)
                .build();

        assertThat(container.resolve(DefaultCustomerService.class), instanceOf(DefaultCustomerService.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_two_constructor_parameters() {
        container = builder.register(DefaultCustomerService.class)
                .construct(CustomerDao.class)
                .construct(CustomerFiller.class)
                .build();

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }

    @Test
    public void should_register_CustomerService_bean_with_different_constructor_parameters() {
        ArrayList<Customer> newCustomers = newArrayList(new Customer(1, "zhangyi"));
        container = builder.register(DefaultCustomerService.class)
                .construct(CustomerDao.class)
                .construct(5)
                .construct("hello melt")
                .construct(50.0)
                .construct(5000L)
                .construct(40.0f)
                .construct(newCustomers)
                .build();

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customerService.getCount(), is(5));
        assertThat(customerService.getMessage(), is("hello melt"));
        assertThat(customerService.getMoney(), is(50.0));
        assertThat(customerService.getId(), is(5000L));
        assertThat(customerService.getSalary(), is(40.0f));
        List<Customer> resultCustomers = customerService.getCustomers();
        assertThat(resultCustomers.size(), is(1));
        assertThat(resultCustomers.get(0).getId(), is(1));
        assertThat(resultCustomers.get(0).getName(), is("zhangyi"));
    }

    @Test
    public void should_register_DefaultBankService_bean_with_one_property() {
        container = builder.register(DefaultBankService.class)
                .withClass(DefaultBankDao.class)
                .build();

        DefaultBankService bankService = container.resolve(DefaultBankService.class);
        assertThat(bankService, not(nullValue()));
        assertThat(bankService.getBankDao(), instanceOf(DefaultBankDao.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_nested_properties() {
        container = builder.register(CustomerDao.class)
                .withClass(JdbcTemplate.class)
                .register(DefaultCustomerService.class)
                .withClass(CustomerDao.class)
                .withClass(CustomerFiller.class)
                .build();

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        CustomerDao customerDao = customerService.getCustomerDao();
        assertThat(customerDao, instanceOf(CustomerDao.class));
        assertThat(customerDao.getJdbcTemplate(), instanceOf(JdbcTemplate.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_two_properties_and_one_int_value_properties() {
        container = builder.register(DefaultCustomerService.class)
                .withClass(CustomerDao.class)
                .withClass(CustomerFiller.class)
                .withValue("count", 3)
                .build();

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getCount(), is(3));

    }

    @Test(expected = BeanConfigurationException.class)
    public void should_throw_configuration_exception_when_register_interface_with_class() {
        container = builder.register(DefaultCustomerService.class)
                .withClass(CustomerDaoInterface.class)
                .withClass(CustomerFiller.class)
                .withValue("count", 3)
                .build();
    }

    @Test
    public void should_register_CustomerService_bean_with_two_properties_and_one_string_value_properties() {
        container = builder.register(DefaultCustomerService.class)
                .withClass(CustomerDao.class)
                .withClass(CustomerFiller.class)
                .withValue("message", "hello melt")
                .build();

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getMessage(), is("hello melt"));
    }

    @Test
    public void should_register_BankService_bean_with_different_properties() {
        List<String> accounts = newArrayList("haha");
        container = builder.register(DefaultBankService.class)
                .withClass(DefaultBankDao.class)
                .withValue("max", 1)
                .withValue("tax", 2.3)
                .withValue("interest", 2.3f)
                .withValue("maxMoney", 12345l)
                .withValue("account", "haha")
                .withValue("accounts", accounts)
                .build();

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
        assertThat(bankService.getMax(), is(1));
        assertThat(bankService.getTax(), is(2.3));
        assertThat(bankService.getInterest(), is(2.3f));
        assertThat(bankService.getMaxMoney(), is(12345l));
        assertThat(bankService.getAccount(), is("haha"));
        assertThat(bankService.getAccounts(), is(accounts));
    }

    @Test
    public void should_have_container_scope() {
        container = builder.register(DefaultBankService.class)
                .build();
        ContainerBuilder childBuilder = new ContainerBuilder();
        Container subContainer = childBuilder.parent(container)
                .register(DefaultBankDao.class)
                .build();
        assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
        assertThat(subContainer.resolve(BankService.class), instanceOf(BankService.class));
        assertThat(container.resolve(BankDao.class), nullValue());
    }


    @Test
    public void should_global_auto_wired_by_type() {
        builder = new ContainerBuilder(AutoWiredBy.TYPE);
        container = builder.register(DefaultBankService.class)
                .register(DefaultBankDao.class)
                .build();

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_auto_wired_by_type_for_each_bean() {
        container = builder.register(DefaultBankService.class)
                .autoWiredBy(AutoWiredBy.TYPE)
                .register(DefaultBankDao.class)
                .build();

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_auto_wired_by_type_from_parent_container() {
        container = builder.register(DefaultBankDao.class)
                .build();
        ContainerBuilder subBuilder = new ContainerBuilder();
        Container subContainer = subBuilder.parent(container)
                .register(DefaultBankService.class)
                .autoWiredBy(AutoWiredBy.TYPE)
                .build();
        assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
        DefaultBankService bankService = subContainer.resolve(BankService.class);
        assertThat(bankService, instanceOf(BankService.class));
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_inject_from_parent_container() {
        container = builder.register(DefaultBankDao.class)
                .build();
        ContainerBuilder subBuilder = new ContainerBuilder();
        Container subContainer = subBuilder.parent(container)
                .register(DefaultBankService.class)
                .autoWiredBy(AutoWiredBy.TYPE)
                .build();
        assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
        DefaultBankService bankService = subContainer.resolve(BankService.class);
        assertThat(bankService, instanceOf(BankService.class));
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
        //TODO
    }

    @Test
    public void should_can_resolve_with_interface_type() {
        container = builder.register(DefaultBankDao.class)
                .build();

        assertThat(container.resolve(BankDao.class), instanceOf(BankDao.class));
        assertThat(container.resolve(BankDao.class), instanceOf(DefaultBankDao.class));
    }

    @Test
    public void should_can_register_to_interface_type_with_class() {
        container = builder.register(DefaultBankService.class)
                .withClass(DefaultBankDao.class)
                .build();

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService, instanceOf(BankService.class));
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_can_register_with_name_and_resolve_by_name() {
        container = builder.register(DefaultBankService.class)
                .asName("bankService")
                .build();

        DefaultBankService bankService = container.resolve("bankService");
        assertThat(bankService, instanceOf(BankService.class));
    }

    @Test
    public void should_can_register_bean_which_implements_two_interfaces() {
        container = builder.register(CustomerDao.class)
                .asName("customerDao")
                .register(DefaultCustomerService.class)
                .withName("customerDao")
                .build();

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, instanceOf(DefaultCustomerService.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDaoInterface.class));
        assertThat(customerService.getCustomerDao(), instanceOf(AnotherCustomerDaoInterface.class));
    }
}
