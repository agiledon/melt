package com.melt.sample.customer.service;

import com.melt.sample.customer.domain.Customer;

public class CustomerFiller {
    public void fill(Customer customer, int id, String name) {
        customer.setId(id);
        customer.setName(name);
    }
}
