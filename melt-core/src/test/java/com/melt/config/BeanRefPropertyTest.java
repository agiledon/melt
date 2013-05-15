package com.melt.config;

import com.melt.config.property.*;
import com.melt.core.InitializedBeans;
import com.melt.core.InjectionContext;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BeanRefPropertyTest {
    private InitializedBeans initializedBeans;
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;
    private DefaultBankService bankService;

    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class);
        bankServiceBeanInfo.addProperty(new BeanRefProperty(bankServiceBeanInfo, "bankDao", "bankDao"));
        bankDao = new DefaultBankDao();
        initializedBeans = new InitializedBeans();
        initializedBeans.addBean("bankDao", bankDao);
        initializedBeans.addBean("bankService", new DefaultBankService());

        bankService = (DefaultBankService) initializedBeans.getBean("bankService");
    }

    @Test
    public void should_set_reference_value_to_bank_service() throws NoSuchMethodException {
        injectProperty();
        assertThat(bankService.getBankDao(), is(bankDao));
    }

    @Test
    public void should_set_max_as_int_property() {
        bankServiceBeanInfo.addProperty(new GenericBeanProperty<Integer>(bankServiceBeanInfo, "max", 1));
        injectProperty();
        assertThat(bankService.getMax(), is(1));
    }

    @Test
    public void should_set_tax_as_double_property() {
        bankServiceBeanInfo.addProperty(new GenericBeanProperty<Double>(bankServiceBeanInfo, "tax", 2.3));
        injectProperty();
        assertThat(bankService.getTax(), is(2.3));
    }

    @Test
    public void should_set_interest_as_float_property() {
        bankServiceBeanInfo.addProperty(new GenericBeanProperty<Float>(bankServiceBeanInfo, "interest", 2.3f));
        injectProperty();
        assertThat(bankService.getInterest(), is(2.3f));
    }

    @Test
    public void should_set_max_money_as_long_property() {
        bankServiceBeanInfo.addProperty(new GenericBeanProperty<Long>(bankServiceBeanInfo, "maxMoney", 12345l));
        injectProperty();
        assertThat(bankService.getMaxMoney(), is(12345l));
    }

    @Test
    public void should_set_account_as_string_property() {
        bankServiceBeanInfo.addProperty(new GenericBeanProperty<String>(bankServiceBeanInfo, "account", "haha"));
        injectProperty();
        assertThat(bankService.getAccount(), is("haha"));
    }

    @Test
    public void should_set_accounts_as_list_property() {
        List<String> accounts = newArrayList("haha");
        bankServiceBeanInfo.addProperty(new GenericBeanProperty<List<String>>(bankServiceBeanInfo, "accounts", accounts));
        injectProperty();
        assertThat(bankService.getAccounts(), is(accounts));
    }

    private void injectProperty() {
        for (BeanProperty beanProperty : bankServiceBeanInfo.getProperties()) {
            beanProperty.injectPropertyValue(new InjectionContext(null, initializedBeans));
        }
    }
}
