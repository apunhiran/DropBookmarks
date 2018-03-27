package com.udemy.dropbookmarks.db;

import com.udemy.dropbookmarks.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by apun.hiran on 3/19/18.
 */
public class UserDAO extends AbstractDAO<User> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> findAll() {
        return list(
                namedQuery(
                        "com.udemy.dropbookmarks.core.User.findAll"
                )
        );
    }

    public Optional<User> findByUsernamePassword(String username, String password) {
        return Optional.ofNullable(
                uniqueResult(
                        namedQuery(
                        "com.udemy.dropbookmarks.core.User.findByUserNamePassword"
                        )
                        .setParameter("username", username)
                        .setParameter("password", password)
                )
        );
    }

    public User save(User user) {
        return persist(user);
    }
}
