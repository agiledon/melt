package com.melt.sample.service;

import com.melt.sample.dao.CustomerDao;
import com.melt.sample.domain.Customer;

import java.util.List;

public class DefaultCustomerService implements CustomerService {

    private CustomerDao customerDao;

    public DefaultCustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public List<Customer> allCustomers() {
        return customerDao.findAll();
    }
}
