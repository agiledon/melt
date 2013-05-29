package com.melt.sample.dao;

import com.melt.sample.model.Order;

public class OrderDao extends GenericDao<Order> {
    @Override
    protected Class getModelClass() {
        return Order.class;
    }
}
