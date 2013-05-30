package com.melt.sample.dao;

import com.melt.sample.model.Customer;

public class CustomerDao extends GenericDao<Customer> {
    @Override
    protected Class getModelClass() {
        return Customer.class;
    }
}
