package com.melt.sample.views.order;

import com.melt.sample.model.Customer;
import com.melt.sample.model.Order;
import com.yammer.dropwizard.views.View;

import java.util.List;
import java.util.Set;

public class OrdersView extends View {
    private final Set<Order> orders;
    private Customer customer;

    public OrdersView(Customer customer) {
        super("index.ftl");
        this.customer = customer;
        this.orders = customer.getOrders();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Set<Order> getOrders() {
        return orders;
    }
}
