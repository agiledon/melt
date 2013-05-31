package com.melt.sample.views.item;

import com.melt.sample.model.Item;
import com.melt.sample.model.Order;
import com.yammer.dropwizard.views.View;

import java.util.List;

public class ItemsView extends View {
    private final int customerId;
    private final Order order;
    private final List<Item> items;

    public ItemsView(int customerId, Order order) {
        super("index.ftl");
        this.customerId = customerId;
        this.order = order;
        this.items = order.getItems();
    }

    public int getCustomerId() {
        return customerId;
    }

    public Order getOrder() {
        return order;
    }

    public List<Item> getItems() {
        return items;
    }
}
