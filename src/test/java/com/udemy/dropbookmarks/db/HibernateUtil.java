package com.udemy.dropbookmarks.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by apun.hiran on 3/19/18.
 */
public class HibernateUtil {

    private static SessionFactory SESSION_FACTORY;

    private static SessionFactory createSessionFactory() {
        SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
        return SESSION_FACTORY;
    }

    public static SessionFactory getSessionFactory() {
        if (SESSION_FACTORY == null)
            SESSION_FACTORY = createSessionFactory();

        return SESSION_FACTORY;
    }

}
