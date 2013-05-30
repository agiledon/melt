package com.melt.sample.resources.order;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.dao.OrderDao;
import com.melt.sample.model.*;
import com.melt.sample.views.IndexView;
import com.melt.sample.views.customer.AddCustomerView;
import com.melt.sample.views.order.AddOrderView;
import com.melt.sample.views.order.OrdersView;
import com.yammer.dropwizard.views.View;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.google.common.collect.FluentIterable.from;
import static com.melt.orm.criteria.By.nil;
import static com.melt.orm.criteria.By.or;

@Path("/customer/{customerId}/order/add")
@Produces(MediaType.TEXT_HTML)
public class AddOrderResource {
    private static final Splitter ITEM_SPLITTER = Splitter.on(",");
    private CustomerDao customerDao;
    private OrderDao orderDao;


    @GET
    public View add(@PathParam("customerId") int customerId) {
        System.out.println("add");
        return new AddOrderView(customerId);
    }

    @POST
    public View submit(@PathParam("customerId") int customerId,
                       @FormParam("count") int count,
                       @FormParam("discount") double discount,
                       @FormParam("sent") boolean sent,
                       @FormParam("orderAddress") String orderAddress,
                       @FormParam("itemPrices") String itemPrices) {
        Customer customer = customerDao.findById(customerId);
        final Order order = new Order();
        order.setCount(count);
        order.setDiscount(discount);
        order.setHasSent(sent);
        order.setOrderAddress(orderAddress);
        order.setBill(createBill());
        order.setCustomer(customer);
        order.setItems(from(ITEM_SPLITTER.split(itemPrices)).transform(createItem(order)).toList());
        orderDao.insert(order);
        return new OrdersView(customer);
    }

    private Bill createBill() {
        Bill bill = new Bill();
        bill.setCount(1000);
        bill.setTitle("ThoughtWorks");
        return bill;
    }

    private Function<String, Item> createItem(final Order order) {
        return new Function<String, Item>() {
            @Override
            public Item apply(String itemPrice) {
                Item item = new Item();
                item.setOrder(order);
                item.setPrice(Float.parseFloat(itemPrice));
                return item;
            }
        };
    }
}
