package com.melt.bean.autowired;

import com.melt.bean.AutoWiredBy;
import com.melt.bean.BeanInfo;
import com.melt.core.InitializedBeans;
import com.melt.exceptions.AutoWiredException;
import com.melt.sample.bank.beans.BankDao;
import com.melt.sample.bank.beans.DefaultBankDao;
import com.melt.sample.bank.beans.DefaultBankService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AutoWiredByTypeTest {
    private BeanInfo bankServiceBeanInfo;
    private BankDao bankDao;
    private InitializedBeans initializedBeans;
    private AutoWiredByType autoWired;


    @Before
    public void setUp() throws Exception {
        bankServiceBeanInfo = new BeanInfo("bankService", DefaultBankService.class);
        bankServiceBeanInfo.setAutoWiredBy(AutoWiredBy.NAME);
        bankDao = new DefaultBankDao();
        initializedBeans = new InitializedBeans();
        initializedBeans.addBean("bankDao", bankDao);
        initializedBeans.addBean("bankDao", DefaultBankDao.class, bankDao);
        initializedBeans.addBean("bankDao", BankDao.class, bankDao);
        initializedBeans.addBean("bankService", new DefaultBankService());
        autoWired = new AutoWiredByType();
    }

    @Test
    public void should_auto_wire_bank_dao_by_type(){
        initializedBeans.addBean("bankDao", bankDao);
        autoWired.autoWired(initializedBeans, bankServiceBeanInfo);
        DefaultBankService bankService = (DefaultBankService) initializedBeans.getBean("bankService");
        assertThat(bankService.getBankDao(), is(bankDao));
    }

    @Test(expected = AutoWiredException.class)
    public void should_throw_exception_when_bank_dao_is_not_right_type(){
        Class bankDaoClass = BankDao.class;
        initializedBeans.addBean("bankDao2", bankDaoClass, new DefaultBankDao());

        autoWired.autoWired(initializedBeans, bankServiceBeanInfo);
    }
}
