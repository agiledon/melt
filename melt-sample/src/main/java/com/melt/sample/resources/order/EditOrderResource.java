package com.melt.sample.resources.order;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.melt.orm.criteria.By;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.dao.OrderDao;
import com.melt.sample.model.Bill;
import com.melt.sample.model.Customer;
import com.melt.sample.model.Item;
import com.melt.sample.model.Order;
import com.melt.sample.views.order.AddOrderView;
import com.melt.sample.views.order.EditOrderView;
import com.melt.sample.views.order.OrdersView;
import com.yammer.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.google.common.collect.FluentIterable.from;

@Path("/customer/{customerId}/order/edit/{orderId}")
@Produces(MediaType.TEXT_HTML)
public class EditOrderResource {
    private CustomerDao customerDao;
    private OrderDao orderDao;


    @GET
    public View add(@PathParam("customerId") int customerId,
                    @PathParam("orderId") int orderId) {
        System.out.println("edit");
        return new EditOrderView(customerId, orderDao.findById(orderId));
    }

    @POST
    public View submit(@PathParam("customerId") int customerId,
                       @PathParam("orderId") int orderId,
                       @FormParam("count") int count,
                       @FormParam("discount") double discount,
                       @FormParam("sent") boolean sent,
                       @FormParam("orderAddress") String orderAddress) {
        final Order order = new Order();
        order.setCount(count);
        order.setDiscount(discount);
        order.setHasSent(sent);
        order.setOrderAddress(orderAddress);
        orderDao.update(order, By.id(orderId));
        return new OrdersView(customerDao.findById(customerId));
    }
}
