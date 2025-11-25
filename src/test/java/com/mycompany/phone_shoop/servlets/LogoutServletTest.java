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
}

