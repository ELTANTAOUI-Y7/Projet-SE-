package com.mycompany.phone_shoop.servlets;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ServletInfoTest {

    @Test
    public void loginServletInfoReturnsShortDescription() {
        assertEquals("Short description", new LoginServlet().getServletInfo());
    }

    @Test
    public void registerServletInfoReturnsShortDescription() {
        assertEquals("Short description", new RegisterServlet().getServletInfo());
    }

    @Test
    public void logoutServletInfoReturnsShortDescription() {
        assertEquals("Short description", new LogoutServlet().getServletInfo());
    }

    @Test
    public void productOperationServletInfoReturnsShortDescription() {
        assertEquals("Short description", new ProductOperationServlet().getServletInfo());
    }
}

