package com.melt.sample.service;

import com.melt.sample.domain.Customer;

public class CustomerFiller {
    public void fill(Customer customer, int id, String name) {
        customer.setId(id);
        customer.setName(name);
    }
}
