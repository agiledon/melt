package com.melt.sample.resources;

import com.melt.sample.dao.CustomerDao;
import com.melt.sample.views.IndexView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.melt.orm.criteria.By.nil;

@Path("/index.html")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {
    private CustomerDao customerDao;

    @GET
    public IndexView index() {
        return new IndexView(customerDao.find(nil()));
    }
}
