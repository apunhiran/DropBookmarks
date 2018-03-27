package com.udemy.dropbookmarks;

import com.udemy.dropbookmarks.auth.DBAuthenticator;
import com.udemy.dropbookmarks.core.Bookmarks;
import com.udemy.dropbookmarks.core.User;
import com.udemy.dropbookmarks.db.BookmarksDAO;
import com.udemy.dropbookmarks.db.UserDAO;
import com.udemy.dropbookmarks.resources.BookmarksResource;
import com.udemy.dropbookmarks.resources.HelloResource;
import com.udemy.dropbookmarks.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;

public class DropBookmarksApplication extends Application<DropBookmarksConfiguration> {

    private final HibernateBundle<DropBookmarksConfiguration> hibernateBundle =
            new HibernateBundle<DropBookmarksConfiguration>(User.class, Bookmarks.class) {
                @Override
                public PooledDataSourceFactory getDataSourceFactory(DropBookmarksConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new DropBookmarksApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropBookmarks";
    }

    @Override
    public void initialize(final Bootstrap<DropBookmarksConfiguration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(new MigrationsBundle<DropBookmarksConfiguration>() {

            @Override
            public PooledDataSourceFactory getDataSourceFactory(DropBookmarksConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final DropBookmarksConfiguration configuration,
                    final Environment environment) {
        final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());

        final BookmarksDAO bookmarksDAO = new BookmarksDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(
                new HelloResource()
        );

        environment.jersey().register(
                new BookmarksResource(bookmarksDAO)
        );
        environment.jersey().register(
                new UserResource(userDAO)
        );
        final DBAuthenticator authenticator
                = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(DBAuthenticator.class,
                        new Class<?>[]{UserDAO.class, SessionFactory.class},
                        new Object[]{userDAO,
                                hibernateBundle.getSessionFactory()});

        // Register authenticator.
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(authenticator)
                        .setAuthorizer(new Authorizer<User>() {
                            @Override
                            public boolean authorize(User principal, String role) {
                                return true;
                            }
                        })
                        .setRealm("SECURITY REALM")
                        .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        //Necessary if @Auth is used to inject a custom Principal
        // type into your resource
        environment.jersey().register(
                new AuthValueFactoryProvider.Binder<>(User.class));
    }

}
