package com.mycompany.phone_shoop.servlets;

import com.mycompany.phone_shoop.entities.User;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegisterServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter body;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        body = new StringWriter();

        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    public void processRequestStopsWhenNameBlank() throws Exception {
        when(request.getParameter("name")).thenReturn("");

        new TestableRegisterServlet().processRequest(request, response);

        assertTrue(body.toString().contains("Name is blank"));
        verify(response, never()).sendRedirect("register.jsp");
    }

    @Test
    public void processRequestPersistsUserAndRedirects() throws Exception {
        when(request.getParameter("name")).thenReturn("Jane");
        when(request.getParameter("email")).thenReturn("jane@mail.com");
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("phone")).thenReturn("123456");
        when(request.getParameter("address")).thenReturn("somewhere");

        TestableRegisterServlet servlet = new TestableRegisterServlet();
        servlet.processRequest(request, response);

        verify(servlet.session).save(org.mockito.ArgumentMatchers.any(User.class));
        verify(session).setAttribute(eq("message"), org.mockito.ArgumentMatchers.contains("Your id : 42"));
        verify(response).sendRedirect("register.jsp");
    }

    @Test
    public void processRequestSetsFailureMessageWhenExceptionOccurs() throws Exception {
        when(request.getParameter("name")).thenReturn("Jane");
        when(request.getParameter("email")).thenReturn("jane@mail.com");
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("phone")).thenReturn("123456");
        when(request.getParameter("address")).thenReturn("somewhere");

        RegisterServlet servlet = new FailingRegisterServlet();
        servlet.processRequest(request, response);

        verify(session).setAttribute(eq("message"), org.mockito.ArgumentMatchers.contains("Something went wrong"));
        verify(response).sendRedirect("register.jsp");
    }

    @Test
    public void doGetDelegatesToProcessRequest() throws Exception {
        TrackingRegisterServlet servlet = new TrackingRegisterServlet();

        servlet.doGet(request, response);

        org.junit.Assert.assertTrue(servlet.called);
    }

    @Test
    public void doPostDelegatesToProcessRequest() throws Exception {
        TrackingRegisterServlet servlet = new TrackingRegisterServlet();

        servlet.doPost(request, response);

        org.junit.Assert.assertTrue(servlet.called);
    }

    private static class TestableRegisterServlet extends RegisterServlet {
        private final SessionFactory sessionFactory;
        private final Session session;
        private final Transaction transaction;

        private TestableRegisterServlet() {
            sessionFactory = mock(SessionFactory.class);
            session = mock(Session.class);
            transaction = mock(Transaction.class);

            when(sessionFactory.openSession()).thenReturn(session);
            when(session.beginTransaction()).thenReturn(transaction);
            when(session.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(42);
        }

        @Override
        protected SessionFactory getSessionFactory() {
            return sessionFactory;
        }

    }

    private static class TrackingRegisterServlet extends RegisterServlet {
        private boolean called;

        @Override
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
            called = true;
        }
    }

    private static class FailingRegisterServlet extends RegisterServlet {
        @Override
        protected SessionFactory getSessionFactory() {
            throw new RuntimeException("fail");
        }
    }
}

