package com.mycompany.phone_shoop.servlets;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogoutServletTest {

    @Test
    public void processRequestClearsCurrentUserAndRedirectsToLogin() throws Exception {
        LogoutServlet servlet = new LogoutServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.processRequest(request, response);

        verify(session).removeAttribute("current-user");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    public void doGetDelegatesToProcessRequest() throws Exception {
        TrackingLogoutServlet servlet = new TrackingLogoutServlet();

        servlet.doGet(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        org.junit.Assert.assertTrue(servlet.called);
    }

    @Test
    public void doPostDelegatesToProcessRequest() throws Exception {
        TrackingLogoutServlet servlet = new TrackingLogoutServlet();

        servlet.doPost(mock(HttpServletRequest.class), mock(HttpServletResponse.class));

        org.junit.Assert.assertTrue(servlet.called);
    }

    private static class TrackingLogoutServlet extends LogoutServlet {
        private boolean called;

        @Override
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
            called = true;
        }
    }
}

