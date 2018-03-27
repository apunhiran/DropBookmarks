package com.udemy.dropbookmarks.db;

/**
 * Created by apun.hiran on 3/19/18.
 */

import com.udemy.dropbookmarks.core.User;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.internal.SessionImpl;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A unit test of class UserDAO.
 *
 * @author Dmitry Noranovich
 */
public class UserDAOTest {

    private static final SessionFactory SESSION_FACTORY
            = HibernateUtil.getSessionFactory();
    private static Liquibase liquibase = null;
    private Session session;
    private Transaction tx;
    private UserDAO dao;

    @BeforeClass
    public static void setUpClass() throws LiquibaseException, SQLException {

        final Session session = SESSION_FACTORY.openSession();
        final SessionImpl sessionImpl = (SessionImpl) session;
        final Connection connection = sessionImpl.connection();
        final Database database = DatabaseFactory
                .getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(connection)
                );
        liquibase
                = new Liquibase(
                "migrations.xml",
                new ClassLoaderResourceAccessor(),
                database);
        session.close();

    }

    @AfterClass
    public static void tearDownClass() {
        SESSION_FACTORY.close();
    }

    @Before
    public void setUp() throws LiquibaseException {
        liquibase.update("DEV");
        session = SESSION_FACTORY.openSession();
        dao = new UserDAO(SESSION_FACTORY);
        tx = null;
    }

    @After
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    /**
     * Test of findAll method, of class UserDAO.
     */
    @Test
    public void testFindAll() {
        List<User> users = null;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            users = dao.findAll();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(users);
        Assert.assertFalse(users.isEmpty());
        Assert.assertEquals(2, users.size());

    }

    /**
     * Test of findByUsernamePassword method, of class UserDAO.
     */
    @Test
    public void testFindByUsernamePassword() {
        String expectedUsername = "user1";
        String expectedPassword = "pwd1";

        java.util.Optional<User> user;

        //First
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            session.createNativeQuery(
                    "insert into users values (null, :username, :password)")
                    .setParameter("username", expectedUsername)
                    .setParameter("password", expectedPassword)
                    .executeUpdate();
            //Do something here with UserDAO

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        //Second
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            user = dao.findByUsernamePassword(expectedUsername, expectedPassword);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }
        Assert.assertNotNull(user);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(expectedUsername, user.get().getUsername());
        Assert.assertEquals(expectedPassword, user.get().getPassword());

    }



}
