package com.melt.sample.resources.customer;

import com.melt.orm.criteria.By;
import com.melt.sample.dao.CustomerDao;
import com.melt.sample.model.Customer;
import com.melt.sample.model.CustomerType;
import com.melt.sample.views.IndexView;
import com.melt.sample.views.customer.AddCustomerView;
import com.yammer.dropwizard.views.View;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static com.melt.orm.criteria.By.nil;

@Path("/customer/add")
@Produces(MediaType.TEXT_HTML)
public class AddCustomerResource {
    private CustomerDao customerDao;

    @GET
    public View add() {
        System.out.println("add");
        return new AddCustomerView();
    }

    @POST
    public View submit(@FormParam("name") String name,
                       @FormParam("age") int age,
                       @FormParam("customerType") String customerType) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAge(age);
        customer.setCustomerType(CustomerType.valueOf(customerType));
        customerDao.insert(customer);
        return new IndexView(customerDao.find(nil()));
    }
}
