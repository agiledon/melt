package com.melt.sample.views;


import com.melt.sample.model.Customer;
import com.yammer.dropwizard.views.View;

import java.util.List;

public class IndexView extends View {
    private List<Customer> customers;

    public IndexView(List<Customer> customers){
        super("index.ftl");
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
