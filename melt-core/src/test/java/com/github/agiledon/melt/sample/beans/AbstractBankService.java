package com.github.agiledon.melt.sample.beans;

import com.github.agiledon.melt.sample.customer.dao.JdbcTemplate;

public abstract class AbstractBankService {
    private JdbcTemplate template;

    public JdbcTemplate getJdbcTemplate() {
        return template;
    }

    public void setJdbcTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
