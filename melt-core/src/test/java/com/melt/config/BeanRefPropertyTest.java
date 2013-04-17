package com.melt.config;

import com.melt.config.property.BeanProperty;
import com.melt.config.property.BeanRefProperty;
import com.melt.core.BeansContainer;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BeanRefPropertyTest {
    private BeansContainer beansContainer;
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;

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
    public void should_set_reference_value_to_bank_service() throws NoSuchMethodException {
        for (BeanProperty beanProperty : bankServiceBeanInfo.getProperties()) {
            beanProperty.setPropertyValue(beansContainer);
        }
        DefaultBankService bankService = (DefaultBankService) beansContainer.resolve("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }
}
