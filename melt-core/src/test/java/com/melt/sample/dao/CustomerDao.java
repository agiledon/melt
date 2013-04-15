package com.melt.sample.dao;

import com.melt.sample.domain.Customer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CustomerDao {
    public List<Customer> findAll() {
        return newArrayList(new Customer(1, "ZhangYi"));
    }
}
