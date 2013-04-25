package com.melt.sample.bank.beans;

import com.melt.sample.customer.dao.JdbcTemplate;

public abstract class AbstractBankService {
    private JdbcTemplate template;

    public JdbcTemplate getJdbcTemplate() {
        return template;
    }

    public void setJdbcTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
