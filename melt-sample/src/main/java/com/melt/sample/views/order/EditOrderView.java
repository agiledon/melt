package com.melt.sample.views.order;

import com.melt.sample.model.Order;
import com.yammer.dropwizard.views.View;

public class EditOrderView extends View {
    private int customerId;
    private final Order order;

    public EditOrderView(int customerId, Order order) {
        super("edit.ftl");
        this.customerId = customerId;
        this.order = order;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Order getOrder() {
        return order;
    }
}
