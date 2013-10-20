package com.github.agiledon.melt.sample.customer.service;

import com.github.agiledon.melt.sample.customer.domain.Customer;

public class CustomerFiller {
    public void fill(Customer customer, int id, String name) {
        customer.setId(id);
        customer.setName(name);
    }
}
