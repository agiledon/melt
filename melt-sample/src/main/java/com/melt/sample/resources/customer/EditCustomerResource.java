package com.melt.sample.resources.customer;

import com.melt.orm.criteria.By;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.model.Customer;
import com.melt.sample.model.CustomerType;
import com.melt.sample.views.IndexView;
import com.yammer.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.melt.orm.criteria.By.nil;

@Path("/customer/edit/{customerId}")
@Produces(MediaType.TEXT_HTML)
public class EditCustomerResource {
    private CustomerDao customerDao;

    @POST
    public View submit(@PathParam("customerId") int customerId,
                       @FormParam("name") String name,
                       @FormParam("age") int age,
                       @FormParam("customerType") String customerType) {
        Customer customer = new Customer();
        customer.setAge(age);
        customer.setName(name);
        customer.setCustomerType(CustomerType.valueOf(customerType));
        customerDao.update(customer, By.id(customerId));
        System.out.println(name);
        System.out.println(age);
        System.out.println(customerId);
        return new IndexView(customerDao.find(nil()));
    }
}
