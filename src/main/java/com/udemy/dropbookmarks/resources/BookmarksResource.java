package com.udemy.dropbookmarks.resources;

import com.udemy.dropbookmarks.core.Bookmarks;
import com.udemy.dropbookmarks.core.User;
import com.udemy.dropbookmarks.db.BookmarksDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

/**
 * Created by apun.hiran on 3/26/18.
 */

@Path("/bookmarks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookmarksResource {

    private BookmarksDAO bookmarksDAO;

    public BookmarksResource(BookmarksDAO bookmarksDAO) {
        this.bookmarksDAO = bookmarksDAO;
    }

    @GET
    @UnitOfWork
    public List<Bookmarks> getBookmarks(@Auth User user) {
        return bookmarksDAO.findForUser(user.getId());
    }

    private Optional<Bookmarks> findIfAuthorized(long bookmarId, long userId ) {
        Optional<Bookmarks> result = bookmarksDAO.findById(bookmarId);
        if(result.isPresent() && userId != result.get().getUser().getId()) {
            throw new ForbiddenException("Not Authorized to see this bookmark");
        }
        return result;
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    public Optional<Bookmarks> getBookmark(@PathParam("id") LongParam id, @Auth User user) {
        return findIfAuthorized(id.get(), user.getId());
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    public Optional<Bookmarks> deleteBookmark (@PathParam("id") LongParam id, @Auth User user) {
        Optional<Bookmarks> bookmarks = findIfAuthorized(id.get(), user.getId());
        if (bookmarks.isPresent()){
            bookmarksDAO.delete(bookmarks.get().getId());
        }
        return bookmarks;
    }

    @POST
    @UnitOfWork
    public Bookmarks saveBookmarks (Bookmarks bookmarks, @Auth User user) {
        bookmarks.setUser(user);
        return bookmarksDAO.save(bookmarks);
    }

    @DELETE
    @UnitOfWork
    @Path("/deleteAll")
    public List<Bookmarks> deleteAllBookmarksForUser (@Auth User user){
        List<Bookmarks> result = getBookmarks(user);
        bookmarksDAO.removeAllForUser(user.getId());
        return result;
    }
    @PUT
    @UnitOfWork
    public Bookmarks updateBookmarks (Bookmarks bookmarks, @Auth User user) {
        bookmarks.setUser(user);
        return bookmarksDAO.save(bookmarks);
    }

}
