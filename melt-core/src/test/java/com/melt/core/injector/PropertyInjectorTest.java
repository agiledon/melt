package com.melt.core.injector;

import com.google.common.collect.ImmutableMap;
import com.melt.config.property.BeanRefProperty;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import com.melt.config.BeanInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.sun.tools.javac.util.List.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PropertyInjectorTest {

    private PropertyInjector injector;
    private Map<String, Object> beans;
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;

    @Before
    public void setUp() throws Exception {
        injector = new PropertyInjector();
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class.getName());
        bankServiceBeanInfo.addProperty(new BeanRefProperty(bankServiceBeanInfo, "bankDao", "bankDao"));
        bankDao = new DefaultBankDao();
        beans = ImmutableMap.of("bankDao", bankDao, "bankService", new DefaultBankService());
    }

    @Test
    public void the_bank_service_should_be_set_the_bank_dao_as_value(){
//        injector.inject(beans, null);
//        DefaultBankService bankService = (DefaultBankService) beans.get("bankService");
//        assertThat(bankService.getBankDao(), is(bankDao));
    }
}
