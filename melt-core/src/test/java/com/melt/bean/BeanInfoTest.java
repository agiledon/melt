package com.melt.bean;

import com.melt.bean.constructor.RefConstructorParameter;
import com.melt.bean.property.BeanRefProperty;
import com.melt.core.InitializedBeans;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static com.sun.tools.javac.util.List.of;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


public class BeanInfoTest {

    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;
    private InitializedBeans initializedBeans;

    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class);
        bankServiceBeanInfo.addProperty(new BeanRefProperty(bankServiceBeanInfo, "bankDao", "bankDao"));
        bankDao = new DefaultBankDao();
        initializedBeans = new InitializedBeans();
        initializedBeans.addBean("bankDao", bankDao);
        initializedBeans.addBean("bankDao", DefaultBankDao.class, bankDao);
        initializedBeans.addBean("bankDao", BankDao.class, bankDao);
        initializedBeans.addBean("bankService", new DefaultBankService());
    }


    @Test
    public void should_return_default_bankDao_bean() {
        Object bean = new BeanInfo("bankDao", DefaultBankDao.class).initialize();
        assertThat(bean, instanceOf(BankDao.class));
    }

    @Test
    public void should_throw_exception_when_bean_config_has_constructor_config() {
        BeanInfo bankDao = new BeanInfo("bankDao", DefaultBankDao.class);
        bankDao.addConstructorParameter(new RefConstructorParameter(0, "ref"));
        Object initialize = bankDao.initialize();
        assertThat(initialize, CoreMatchers.is(CoreMatchers.nullValue()));
    }

    @Test
    public void the_bank_service_should_be_set_the_bank_dao_as_value(){
        bankServiceBeanInfo.injectProperties(null, initializedBeans);
        DefaultBankService bankService = (DefaultBankService) initializedBeans.getBean("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }

    @Test
    public void should_auto_wire_bank_dao_to_bank_service_by_name(){
        bankServiceBeanInfo.setAutoWiredBy(AutoWiredBy.NAME);
        bankServiceBeanInfo.autoWiredProperties(null, initializedBeans);
        DefaultBankService bankService = (DefaultBankService) initializedBeans.getBean("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }

    @Test
    public void should_auto_wire_bank_dao_to_bank_service_by_type(){
        bankServiceBeanInfo.setAutoWiredBy(AutoWiredBy.TYPE);
        bankServiceBeanInfo.autoWiredProperties(null, initializedBeans);
        DefaultBankService bankService = (DefaultBankService) initializedBeans.getBean("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }

    @Test
    public void should_not_auto_wire_bank_dao_to_bank_service_by_null(){
        bankServiceBeanInfo.setAutoWiredBy(AutoWiredBy.NULL);
        bankServiceBeanInfo.autoWiredProperties(null, initializedBeans);
        DefaultBankService bankService = (DefaultBankService) initializedBeans.getBean("bankService");
        assertThat(bankService.getBankDao(), nullValue());
    }
}
