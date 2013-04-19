package com.melt.config;

import com.melt.config.property.BeanRefProperty;
import com.melt.core.BeansContainer;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import static com.sun.tools.javac.util.List.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class BeanInfoTest {

    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;
    private BeansContainer beansContainer;

    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class);
        bankServiceBeanInfo.addProperty(new BeanRefProperty(bankServiceBeanInfo, "bankDao", "bankDao"));
        bankDao = new DefaultBankDao();
        beansContainer = new BeansContainer();
        beansContainer.addBean("bankDao", bankDao);
        beansContainer.addBean("bankService", new DefaultBankService());
    }

    @Test
    public void the_bank_service_should_be_set_the_bank_dao_as_value(){
        bankServiceBeanInfo.injectProperties(beansContainer);
        DefaultBankService bankService = (DefaultBankService) beansContainer.resolve("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }
}
