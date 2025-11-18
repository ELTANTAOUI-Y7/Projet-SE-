package com.mycompany.phone_shoop.Dao;

import com.mycompany.phone_shoop.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDaoTest {

    private SessionFactory factory;
    private Session session;
    private Query query;
    private UserDao userDao;

    @Before
    public void setUp() {
        factory = mock(SessionFactory.class);
        session = mock(Session.class);
        query = mock(Query.class);
        when(factory.openSession()).thenReturn(session);
        when(session.createQuery("from User where user_email =:e and user_password=:p")).thenReturn(query);
        userDao = new UserDao(factory);
    }

    @Test
    public void getUserByEmailAndPasswordReturnsMatchingUser() {
        User user = new User();
        when(query.setParameter("e", "mail@host.com")).thenReturn(query);
        when(query.setParameter("p", "secret")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        User result = userDao.getUserByEmailAndPassword("mail@host.com", "secret");

        assertSame(user, result);
        verify(session).close();
    }
}

