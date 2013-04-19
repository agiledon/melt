package com.melt.config;

import com.melt.config.property.*;
import com.melt.core.BeansContainer;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BeanRefPropertyTest {
    private BeansContainer beansContainer;
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;
    private DefaultBankService bankService;

    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class);
        bankServiceBeanInfo.addProperty(new BeanRefProperty(bankServiceBeanInfo, "bankDao", "bankDao"));
        bankDao = new DefaultBankDao();
        beansContainer = new BeansContainer();
        beansContainer.addBean("bankDao", bankDao);
        beansContainer.addBean("bankService", new DefaultBankService());

        bankService = (DefaultBankService) beansContainer.resolve("bankService");
    }

    @Test
    public void should_set_reference_value_to_bank_service() throws NoSuchMethodException {
        injectProperty();
        assertThat(bankService.getBankDao(), is(bankDao));
    }

    @Test
    public void should_set_max_as_int_property() {
        bankServiceBeanInfo.addProperty(new BeanIntProperty("max", bankServiceBeanInfo, 1));
        injectProperty();
        assertThat(bankService.getMax(), is(1));
    }

    @Test
    public void should_set_tax_as_double_property() {
        bankServiceBeanInfo.addProperty(new BeanDoubleProperty("tax", bankServiceBeanInfo, 2.3));
        injectProperty();
        assertThat(bankService.getTax(), is(2.3));
    }

    @Test
    public void should_set_interest_as_float_property() {
        bankServiceBeanInfo.addProperty(new BeanFloatProperty("interest", bankServiceBeanInfo, 2.3f));
        injectProperty();
        assertThat(bankService.getInterest(), is(2.3f));
    }

    @Test
    public void should_set_max_money_as_long_property() {
        bankServiceBeanInfo.addProperty(new BeanLongProperty("maxMoney", bankServiceBeanInfo, 12345l));
        injectProperty();
        assertThat(bankService.getMaxMoney(), is(12345l));
    }

    @Test
    public void should_set_account_as_string_property() {
        bankServiceBeanInfo.addProperty(new BeanStringProperty("account", bankServiceBeanInfo, "haha"));
        injectProperty();
        assertThat(bankService.getAccount(), is("haha"));
    }

    @Test
    public void should_set_accounts_as_list_property() {
        List<String> accounts = newArrayList("haha");
        bankServiceBeanInfo.addProperty(new BeanObjectProperty("accounts", bankServiceBeanInfo, accounts));
        injectProperty();
        assertThat(bankService.getAccounts(), is(accounts));
    }

    private void injectProperty() {
        for (BeanProperty beanProperty : bankServiceBeanInfo.getProperties()) {
            beanProperty.injectPropertyValue(beansContainer);
        }
    }
}
