package com.melt.sample.views.order;

import com.yammer.dropwizard.views.View;

public class AddOrderView extends View {
    private int customerId;

    public AddOrderView(int customerId) {
        super("add.ftl");
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }
}
