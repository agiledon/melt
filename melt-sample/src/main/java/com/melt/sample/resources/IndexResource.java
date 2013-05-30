package com.melt.sample.resources;

import com.melt.sample.views.IndexView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/index.html")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {
    @GET
    public IndexView index() {
        return new IndexView();
    }
}
