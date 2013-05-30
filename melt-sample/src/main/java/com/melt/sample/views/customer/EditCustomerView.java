package com.melt.sample.views.customer;

import com.melt.sample.model.Customer;
import com.yammer.dropwizard.views.View;

public class EditCustomerView extends View {

    private Customer customer;

    public EditCustomerView(Customer customer) {
        super("edit.ftl");
        this.customer = customer;
        System.out.println(customer.getName());
    }

    public Customer getCustomer() {
        return customer;
    }
}
