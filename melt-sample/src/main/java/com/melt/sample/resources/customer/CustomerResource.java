package com.melt.sample.resources.customer;

import com.melt.sample.dao.CustomerDao;
import com.melt.sample.views.customer.EditCustomerView;
import com.yammer.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/customer/{customerId}")
@Produces(MediaType.TEXT_HTML)
public class CustomerResource {
    private CustomerDao customerDao;

    @GET
    public View edit(@PathParam("customerId") int customerId) {
        return new EditCustomerView(customerDao.findById(customerId));
    }
}
