package com.udemy.dropbookmarks.resources;

import com.udemy.dropbookmarks.core.User;
import com.udemy.dropbookmarks.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by apun.hiran on 3/26/18.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @UnitOfWork
    public User addUser(@Auth User user, User newUser) {
        if (!user.getUsername().equalsIgnoreCase("admin")) {
            throw new ForbiddenException("Not Authorized to add user");
        }
        return userDAO.save(newUser);
    }

    @GET
    @UnitOfWork
    public List<User> listUsers(@Auth User user) {
        if (!user.getUsername().equalsIgnoreCase("admin")) {
            throw new ForbiddenException("Not Admin User");
        }
        return userDAO.findAll();
    }
}
