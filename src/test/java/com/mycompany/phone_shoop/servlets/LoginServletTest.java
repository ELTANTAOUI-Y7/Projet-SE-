package com.mycompany.phone_shoop.servlets;

import com.mycompany.phone_shoop.Dao.UserDao;
import com.mycompany.phone_shoop.entities.User;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        stringWriter = new StringWriter();

        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getParameter("email")).thenReturn("mail@host.com");
        when(request.getParameter("password")).thenReturn("secret");
    }

    @Test
    public void processRequestRedirectsToLoginWhenUserNotFound() throws Exception {
        TestableLoginServlet servlet = new TestableLoginServlet(null);

        servlet.processRequest(request, response);

        verify(session).setAttribute("message", "Invalid details !! Try with another one");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    public void processRequestRedirectsAdminUsersToAdminDashboard() throws Exception {
        User admin = new User();
        admin.setUserType("admin");
        TestableLoginServlet servlet = new TestableLoginServlet(admin);

        servlet.processRequest(request, response);

        verify(session).setAttribute("current-user", admin);
        verify(response).sendRedirect("admin.jsp");
    }

    @Test
    public void processRequestRedirectsNormalUsersToIndex() throws Exception {
        User normal = new User();
        normal.setUserType("normal");
        TestableLoginServlet servlet = new TestableLoginServlet(normal);

        servlet.processRequest(request, response);

        verify(session).setAttribute("current-user", normal);
        verify(response).sendRedirect("index.jsp");
    }

    private static class TestableLoginServlet extends LoginServlet {

        private final User userToReturn;

        private TestableLoginServlet(User userToReturn) {
            this.userToReturn = userToReturn;
        }

        @Override
        protected UserDao createUserDao() {
            UserDao userDao = mock(UserDao.class);
            when(userDao.getUserByEmailAndPassword("mail@host.com", "secret")).thenReturn(userToReturn);
            return userDao;
        }
    }
}

