package com.udemy.dropbookmarks.resources;

import com.udemy.dropbookmarks.core.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by apun.hiran on 3/2/18.
 */

@Path("/hello")
public class HelloResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @UnitOfWork
    public String getGreetings() {
        return "Hello World!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/secured")
    @UnitOfWork
    public String getSecuredGeeting(@Auth User user) {
        return "Hello Secured World!";
    }
}
