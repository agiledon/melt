package com.melt.sample.customer.service;

import com.melt.sample.customer.dao.CustomerDao;
import com.melt.sample.customer.domain.Customer;

import java.util.List;

public class DefaultCustomerService implements CustomerService {

    private CustomerDao customerDao;
    private CustomerFiller customerFiller;

    public DefaultCustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public DefaultCustomerService(CustomerDao customerDao, CustomerFiller customerFiller) {
        this.customerDao = customerDao;
        this.customerFiller = customerFiller;
    }

    @Override
    public List<Customer> allCustomers() {
        List<Customer> customers = customerDao.findAll();
        if (customerFiller != null) {
            for (Customer customer : customers) {
                int id = 1;
                String name = "zhangyi";
                customerFiller.fill(customer, id, name);
            }
        }
        return customers;
    }
}
