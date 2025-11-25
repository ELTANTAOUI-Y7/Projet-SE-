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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private UserDao userDao;
    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        userDao = mock(UserDao.class);
        writer = new PrintWriter(new StringWriter());

        when(request.getParameter("email")).thenReturn("mail@host.com");
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void redirectsBackToLoginWhenCredentialsInvalid() throws Exception {
        when(userDao.getUserByEmailAndPassword("mail@host.com", "secret")).thenReturn(null);
        LoginServlet servlet = new TestableLoginServlet(userDao);

        servlet.doPost(request, response);

        verify(session).setAttribute("message", "Invalid details !! Try with another one");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    public void adminUsersRedirectedToAdminPage() throws Exception {
        User user = new User();
        user.setUserType("admin");
        when(userDao.getUserByEmailAndPassword("mail@host.com", "secret")).thenReturn(user);
        LoginServlet servlet = new TestableLoginServlet(userDao);

        servlet.doPost(request, response);

        verify(session).setAttribute("current-user", user);
        verify(response).sendRedirect("admin.jsp");
    }

    @Test
    public void normalUsersRedirectedToIndexPage() throws Exception {
        User user = new User();
        user.setUserType("normal");
        when(userDao.getUserByEmailAndPassword("mail@host.com", "secret")).thenReturn(user);
        LoginServlet servlet = new TestableLoginServlet(userDao);

        servlet.doPost(request, response);

        verify(session).setAttribute("current-user", user);
        verify(response).sendRedirect("index.jsp");
    }

    private static class TestableLoginServlet extends LoginServlet {

        private final UserDao userDao;

        private TestableLoginServlet(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected UserDao createUserDao() {
            return userDao;
        }
    }
}

