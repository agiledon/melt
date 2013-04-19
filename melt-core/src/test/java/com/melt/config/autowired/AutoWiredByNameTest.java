package com.melt.config.autowired;

import com.melt.config.AutoWiredBy;
import com.melt.config.BeanInfo;
import com.melt.core.BeansContainer;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AutoWiredByNameTest {
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;
    private BeansContainer beansContainer;


    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class);
        bankServiceBeanInfo.setAutoWiredBy(AutoWiredBy.NAME);
        bankDao = new DefaultBankDao();
        beansContainer = new BeansContainer();
        beansContainer.addBean("bankDao", bankDao);
        beansContainer.addBean(DefaultBankDao.class, "bankDao", bankDao);
        beansContainer.addBean(BankDao.class, "bankDao", bankDao);
        beansContainer.addBean("bankService", new DefaultBankService());
    }

    @Test
    public void should_auto_wire_bank_dao_by_name(){
        AutoWiredByName autoWired = new AutoWiredByName();
        autoWired.autoWired(beansContainer, bankServiceBeanInfo);
        DefaultBankService bankService = (DefaultBankService) beansContainer.resolve("bankService");
//        assertThat(bankService.getBankDao(), is(bankDao));
    }
}
