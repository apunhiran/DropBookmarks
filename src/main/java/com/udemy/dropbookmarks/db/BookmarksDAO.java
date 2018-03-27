package com.udemy.dropbookmarks.db;

import com.udemy.dropbookmarks.core.Bookmarks;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by apun.hiran on 3/20/18.
 */
public class BookmarksDAO extends AbstractDAO<Bookmarks>{

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public BookmarksDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Bookmarks> findForUser(long id) {
        return list(
                namedQuery(
                        "com.udemy.dropbookmarks.core.Bookmarks.findForUser"
                ).setParameter("id", id)
        );
    }
    public Optional<Bookmarks> findById(long id) {

        return Optional.ofNullable(get(id));
    }

    /**
     * Create or Update a bookmark.
     *
     * @param bookmark a bookmark to be saved
     * @return the saved bookmark with all auto-generated fields filled.
     */
    public Bookmarks save(Bookmarks bookmark) {
        return persist(bookmark);
    }

    public void delete(long id) {
        namedQuery("com.udemy.dropbookmarks.core.Bookmarks.remove")
                .setParameter("id",id)
                .executeUpdate();
    }

    public void removeAllForUser( long id) {
        namedQuery("com.udemy.dropbookmarks.core.Bookmarks.removeAllForUser")
                .setParameter("userId", id)
                .executeUpdate();
    }

}
