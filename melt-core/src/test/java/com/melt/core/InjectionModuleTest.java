package com.melt.core;

import com.melt.config.autowired.AutoWiredBy;
import com.melt.exceptions.BeanConfigurationException;
import com.melt.exceptions.MoreThanOneClassRegisteredException;
import com.melt.sample.bank.beans.*;
import com.melt.sample.customer.dao.*;
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

public class InjectionModuleTest {

    private InjectionModule builder;
    private Container container;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void should_register_CustomerService_bean() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultCustomerService.class);
            }
        });
        assertThat(container.resolve(DefaultCustomerService.class), instanceOf(DefaultCustomerService.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_two_constructor_parameters() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultCustomerService.class)
                        .withConstructorParameter(CustomerDao.class)
                        .withConstructorParameter(CustomerFiller.class);
            }
        });

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }

    @Test
    public void should_register_CustomerService_bean_with_different_constructor_parameters() {
        container = Melt.createContainer(new InjectionModule() {
            ArrayList<Customer> newCustomers = newArrayList(new Customer(1, "zhangyi"));

            @Override
            public void configure() {
                register(DefaultCustomerService.class)
                        .withConstructorParameter(CustomerDao.class)
                        .withConstructorParameter(5)
                        .withConstructorParameter("hello melt")
                        .withConstructorParameter(50.0)
                        .withConstructorParameter(5000L)
                        .withConstructorParameter(40.0f)
                        .withConstructorParameter(newCustomers);
            }
        });

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
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .withProperty(DefaultBankDao.class);
            }
        });

        DefaultBankService bankService = container.resolve(DefaultBankService.class);
        assertThat(bankService, not(nullValue()));
        assertThat(bankService.getBankDao(), instanceOf(DefaultBankDao.class));
    }

    @Test
    public void should_register_DefaultBankService_bean_with_super_property() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .withProperty(DefaultBankDao.class)
                        .withProperty(JdbcTemplate.class);
            }
        });

        DefaultBankService bankService = container.resolve(DefaultBankService.class);
        assertThat(bankService, not(nullValue()));
        assertThat(bankService.getBankDao(), instanceOf(DefaultBankDao.class));
        assertThat(bankService.getJdbcTemplate(), instanceOf(JdbcTemplate.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_nested_properties() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(CustomerDao.class)
                        .withProperty(JdbcTemplate.class);
                register(DefaultCustomerService.class)
                        .withProperty(CustomerDao.class)
                        .withProperty(CustomerFiller.class);
            }
        });

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        CustomerDao customerDao = customerService.getCustomerDao();
        assertThat(customerDao, instanceOf(CustomerDao.class));
        assertThat(customerDao.getJdbcTemplate(), instanceOf(JdbcTemplate.class));
    }

    @Test
    public void should_register_CustomerService_bean_with_two_properties_and_one_int_value_properties() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultCustomerService.class)
                        .withProperty(CustomerDao.class)
                        .withProperty(CustomerFiller.class)
                        .withProperty("count", 3);
            }
        });

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getCount(), is(3));

    }

    @Test(expected = BeanConfigurationException.class)
    public void should_throw_configuration_exception_when_register_interface_with_class() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultCustomerService.class)
                        .withProperty(CustomerDaoInterface.class)
                        .withProperty(CustomerFiller.class)
                        .withProperty("count", 3);
            }
        });
    }

    @Test
    public void should_register_CustomerService_bean_with_two_properties_and_one_string_value_properties() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultCustomerService.class)
                        .withProperty(CustomerDao.class)
                        .withProperty(CustomerFiller.class)
                        .withProperty("message", "hello melt");
            }
        });

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        assertThat(customerService.getCustomerFiller(), instanceOf(CustomerFiller.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getMessage(), is("hello melt"));
    }

    @Test
    public void should_register_BankService_bean_with_different_properties() {
        final List<String> accounts = newArrayList("haha");
        container = Melt.createContainer(new InjectionModule() {

            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .withProperty(DefaultBankDao.class)
                        .withProperty("max", 1)
                        .withProperty("tax", 2.3)
                        .withProperty("interest", 2.3f)
                        .withProperty("maxMoney", 12345l)
                        .withProperty("account", "haha")
                        .withProperty("accounts", accounts);
            }
        });

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
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class);
            }
        });

        Container subContainer = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankDao.class);
            }
        }, container);
        assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
        assertThat(subContainer.resolve(BankService.class), instanceOf(BankService.class));
        assertThat(container.resolve(BankDao.class), nullValue());
    }


    @Test
    public void should_global_auto_wired_by_type() {
        container = Melt.createContainer(new InjectionModule(AutoWiredBy.TYPE) {
            @Override
            public void configure() {
                register(DefaultBankService.class);
                register(DefaultBankDao.class);
            }
        });

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_auto_wired_by_type_for_each_bean() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .autoWiredBy(AutoWiredBy.TYPE);
                register(DefaultBankDao.class);
            }
        });

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_auto_wired_by_type_from_parent_container() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankDao.class);
            }
        });

        Container subContainer = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .autoWiredBy(AutoWiredBy.TYPE);
            }
        }, container);

        assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
        DefaultBankService bankService = subContainer.resolve(BankService.class);
        assertThat(bankService, instanceOf(BankService.class));
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }


    @Test
    public void should_inject_from_parent_container_by_name() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankDao.class)
                        .asName("bankDao");
            }
        });
        Container subContainer = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .autoWiredBy(AutoWiredBy.NAME);
            }
        }, container);

        assertThat(subContainer.resolve(BankDao.class), instanceOf(BankDao.class));
        DefaultBankService bankService = subContainer.resolve(BankService.class);
        assertThat(bankService, instanceOf(BankService.class));
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_resolve_with_interface_type() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankDao.class);
            }
        });

        assertThat(container.resolve(BankDao.class), instanceOf(BankDao.class));
        assertThat(container.resolve(BankDao.class), instanceOf(DefaultBankDao.class));
    }

    @Test
    public void should_register_to_interface_type_with_class() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .withProperty(DefaultBankDao.class);
            }
        });

        DefaultBankService bankService = container.resolve(BankService.class);
        assertThat(bankService, instanceOf(BankService.class));
        assertThat(bankService.getBankDao(), instanceOf(BankDao.class));
    }

    @Test
    public void should_register_with_name_and_resolve_by_name() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankService.class)
                        .asName("bankService");
            }
        });

        DefaultBankService bankService = container.resolve("bankService");
        assertThat(bankService, instanceOf(BankService.class));
    }

    @Test
    public void should_register_bean_which_implements_two_interfaces() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(CustomerDao.class)
                        .asName("customerDao");
                register(DefaultCustomerService.class)
                        .withRefProperty("customerDao", "customerDao");
            }
        });

        DefaultCustomerService customerService = container.resolve(DefaultCustomerService.class);
        assertThat(customerService, instanceOf(DefaultCustomerService.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDao.class));
        assertThat(customerService.getCustomerDao(), instanceOf(CustomerDaoInterface.class));
        assertThat(customerService.getCustomerDao(), instanceOf(AnotherCustomerDaoInterface.class));
    }

    @Test
    public void should_register_bean_from_factory() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(Factory.class)
                        .factory("init");
            }
        });

        FactoryService factoryService = container.resolve(FactoryService.class);
        assertThat(factoryService, instanceOf(FactoryService.class));
    }

    @Test(expected = MoreThanOneClassRegisteredException.class)
    public void should_throw_exception_when_register_same_class_without_name() {
        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultBankDao.class);
                register(DefaultBankDao.class);
                register(DefaultCustomerService.class);
            }
        });
    }

    @Test
    public void should_register_CustomerService_bean_with_two_constructor_parameters_from_parent_container() {

        container = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(CustomerDao.class)
                        .asName("customerDao");
                register(CustomerFiller.class)
                        .asName("customerFiller");
            }
        });

        Container childContainer = Melt.createContainer(new InjectionModule() {
            @Override
            public void configure() {
                register(DefaultCustomerService.class)
                        .withRefConstructorParameter("customerDao")
                        .withRefConstructorParameter("customerFiller");
            }
        }, container);

        DefaultCustomerService customerService = childContainer.resolve(DefaultCustomerService.class);
        assertThat(customerService, not(nullValue()));
        List<Customer> customers = customerService.allCustomers();
        assertThat(customers.size(), is(1));
        assertThat(customers.get(0).getId(), is(1));
        assertThat(customers.get(0).getName(), is("zhangyi"));
    }
}
