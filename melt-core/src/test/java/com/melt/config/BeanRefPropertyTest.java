package com.melt.config;

import com.google.common.collect.ImmutableMap;
import com.melt.config.property.BeanProperty;
import com.melt.config.property.BeanRefProperty;
import com.melt.core.BeansContainer;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BeanRefPropertyTest {
    private BeansContainer beansContainer;
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;

    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class.getName());
        bankServiceBeanInfo.addProperty(new BeanRefProperty(bankServiceBeanInfo, "bankDao", "bankDao"));
        bankDao = new DefaultBankDao();
        beansContainer = new BeansContainer();
        beansContainer.addBean("bankDao", bankDao);
        beansContainer.addBean("bankService", new DefaultBankService());
        beansContainer.addClass("bankService", DefaultBankService.class);
    }

    @Test
    public void should_set_reference_value_to_bank_service() throws NoSuchMethodException {
        for (BeanProperty beanProperty : bankServiceBeanInfo.getProperties()) {
            beanProperty.injectPropertyValue(beansContainer);
        }
        DefaultBankService bankService = (DefaultBankService) beansContainer.get("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }
}
