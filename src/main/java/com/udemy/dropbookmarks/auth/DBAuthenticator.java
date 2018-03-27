package com.udemy.dropbookmarks.auth;

import com.udemy.dropbookmarks.core.User;
import com.udemy.dropbookmarks.db.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * Created by apun.hiran on 3/20/18.
 */
public class DBAuthenticator
        implements Authenticator<BasicCredentials, User> {

    private UserDAO userDAO;

    public DBAuthenticator(UserDAO userDAO, SessionFactory sessionFactory) {
        this.userDAO = userDAO;
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    @Override
    @UnitOfWork
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return userDAO.findByUsernamePassword(credentials.getUsername(),credentials.getPassword());
    }
}
