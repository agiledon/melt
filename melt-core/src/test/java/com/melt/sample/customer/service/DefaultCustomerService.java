package com.melt.sample.customer.service;

import com.melt.sample.customer.dao.CustomerDao;
import com.melt.sample.customer.domain.Customer;

import java.util.ArrayList;
import java.util.List;

public class DefaultCustomerService implements CustomerService {

    private CustomerDao customerDao;
    private CustomerFiller customerFiller;
    private int count;
    private String message;
    private double money;
    private long id;
    private float salary;
    private List<Customer> customers;

    public double getMoney() {
        return money;
    }

    public long getId() {
        return id;
    }

    public float getSalary() {
        return salary;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public DefaultCustomerService() {
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public CustomerFiller getCustomerFiller() {
        return customerFiller;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void setCustomerFiller(CustomerFiller customerFiller) {
        this.customerFiller = customerFiller;
    }

    public DefaultCustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public DefaultCustomerService(CustomerDao customerDao, CustomerFiller customerFiller) {
        this.customerDao = customerDao;
        this.customerFiller = customerFiller;
    }

    public DefaultCustomerService(CustomerDao customerDao, int count, String message, double money, long id, float salary, ArrayList<Customer> customers) {
        this.customerDao = customerDao;
        this.count = count;
        this.message = message;
        this.money = money;
        this.id = id;
        this.salary = salary;
        this.customers = customers;
    }

    public void setCount(int count) {

        this.count = count;
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

    public int getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
