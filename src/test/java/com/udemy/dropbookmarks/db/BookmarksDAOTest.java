package com.udemy.dropbookmarks.db;

import com.udemy.dropbookmarks.core.Bookmarks;
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

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by apun.hiran on 3/20/18.
 */
public class BookmarksDAOTest {
    private static final SessionFactory SESSION_FACTORY
            = HibernateUtil.getSessionFactory();
    private static Liquibase liquibase = null;
    private Session session;
    private Transaction tx;
    private BookmarksDAO bookmarksDAO;
    private UserDAO userDAO;

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
        bookmarksDAO = new BookmarksDAO(SESSION_FACTORY);
        userDAO = new UserDAO(SESSION_FACTORY);
        tx = null;
    }

    @After
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    @Test
    public void testFindForUser() {
        List<Bookmarks> bookmarks = null;
        int id = 1;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            bookmarks = bookmarksDAO.findForUser(id);

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

        Assert.assertNotNull(bookmarks);
        Assert.assertFalse(bookmarks.isEmpty());
        Assert.assertEquals(2, bookmarks.size());

    }

    @Test
    public void testSaveBookmark() {
        User user = new User("test1", "test1");
        Bookmarks bookmarks = new Bookmarks("Test1", "www.xyz.com", "Test1", user );
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();
            userDAO.save(user);
            userDAO.findByUsernamePassword("test1", "test1");
            bookmarksDAO.save(bookmarks);
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

        Assert.assertNotNull(bookmarks.getName(),"Test1");
        //Assert.assertFalse(bookmarks.isEmpty());
        //Assert.assertEquals(2, bookmarks.size());

    }

    @Test
    public void testFindById() {

        String expectedName = "Test1";
        String expectedUrl = "www.abc.com";
        String expectedDescription = "Test1";
        long userId = 1;
        long bookmarkId;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();
            session.createNativeQuery(
                    "insert into bookmarks values " +
                            "(NULL, :expectedName, :expectedUrl, :expectedDescription, :userId)"
            ).setParameter("expectedName", expectedName)
             .setParameter("expectedUrl", expectedUrl)
             .setParameter("expectedDescription", expectedDescription)
             .setParameter("userId", userId)
             .executeUpdate();

            BigInteger result = (BigInteger)
                    session.createNativeQuery(
                            "select id from bookmarks "
                                    + "where url = :url "
                                    + "and description = :description "
                                    + "and user_id = :userId"
                    )
                    .setParameter("url", expectedUrl)
                    .setParameter("description", expectedDescription)
                    .setParameter("userId", userId)
                    .uniqueResult();
            bookmarkId = result.intValue();
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

        Assert.assertNotNull(bookmarkId);
        //Assert.assertFalse(bookmarks.isEmpty());
        //Assert.assertEquals(2, bookmarks.size());

    }
}
