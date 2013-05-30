package com.melt.sample.resources.customer;

import com.melt.orm.criteria.By;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.model.Customer;
import com.melt.sample.views.IndexView;
import com.yammer.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.melt.orm.criteria.By.id;
import static com.melt.orm.criteria.By.nil;

@Path("/customer/delete/{customerId}")
@Produces(MediaType.TEXT_HTML)
public class DeleteCustomerResource {
    private CustomerDao customerDao;

    @GET
    public View submit(@PathParam("customerId") int customerId) {
        customerDao.delete(id(customerId));
        System.out.println(customerId);
        return new IndexView(customerDao.find(nil()));
    }
}
