package com.melt.sample.resources.customer;


import com.melt.orm.criteria.By;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.dao.OrderDao;
import com.melt.sample.views.order.OrdersView;import com.yammer.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/customer/orders/{customerId}")
@Produces(MediaType.TEXT_HTML)
public class CustomerOrdersResource {
    private CustomerDao customerDao;
    @GET
    public View orders(@PathParam("customerId") int customerId) {
        return new OrdersView(customerDao.findById(customerId));
    }
}
