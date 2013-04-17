package com.melt.sample.customer.dao;

import com.melt.sample.customer.domain.Customer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CustomerDao {
    public List<Customer> findAll() {
        return newArrayList(new Customer());
    }
}
