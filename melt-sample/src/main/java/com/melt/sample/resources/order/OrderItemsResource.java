package com.melt.sample.resources.order;


import com.melt.sample.dao.CustomerDao;
import com.melt.sample.dao.OrderDao;
import com.melt.sample.views.item.ItemsView;
import com.melt.sample.views.order.OrdersView;
import com.yammer.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/customer/{customerId}/order/{orderId}/items")
@Produces(MediaType.TEXT_HTML)
public class OrderItemsResource {
    private CustomerDao customerDao;
    private OrderDao orderDao;
    @GET
    public View orders(@PathParam("customerId") int customerId,
                       @PathParam("orderId") int orderId) {
        return new ItemsView(customerId, orderDao.findById(orderId));
    }
}
