package com.mycompany.phone_shoop.servlets;

import com.mycompany.phone_shoop.Dao.CategoryDao;
import com.mycompany.phone_shoop.Dao.ProductDao;
import com.mycompany.phone_shoop.entities.Category;
import com.mycompany.phone_shoop.entities.Product;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductOperationServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ServletContext servletContext;
    private StringWriter writer;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        servletContext = mock(ServletContext.class);
        writer = new StringWriter();

        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(servletContext);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    public void invalidOperationSetsMessage() throws Exception {
        when(request.getParameter("operation")).thenReturn(null);

        TestableProductOperationServlet servlet = new TestableProductOperationServlet();
        servlet.processRequest(request, response);

        verify(session).setAttribute("message", "Invalid operation");
        verify(response).sendRedirect("admin.jsp");
    }

    @Test
    public void unknownOperationSetsFallbackMessage() throws Exception {
        when(request.getParameter("operation")).thenReturn("removeProduct");

        TestableProductOperationServlet servlet = new TestableProductOperationServlet();
        servlet.processRequest(request, response);

        verify(session).setAttribute("message", "Unknown operation");
        verify(response).sendRedirect("admin.jsp");
    }

    @Test
    public void addCategoryPersistsCategoryAndRedirects() throws Exception {
        when(request.getParameter("operation")).thenReturn("addCategory");
        when(request.getParameter("catTitle")).thenReturn("Phones");
        when(request.getParameter("catDescription")).thenReturn("Handsets");

        TestableProductOperationServlet servlet = new TestableProductOperationServlet();

        servlet.processRequest(request, response);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(servlet.categoryDao).saveCategory(captor.capture());
        assertEquals("Phones", captor.getValue().getCategoryTitle());
        verify(session).setAttribute("message", "A new Category has been added successufly ");
        verify(response).sendRedirect("admin.jsp");
    }

    @Test
    public void addProductPersistsProductAndUploadsImage() throws Exception {
        when(request.getParameter("operation")).thenReturn("addProduct");
        when(request.getParameter("pName")).thenReturn("Pixel");
        when(request.getParameter("pDesc")).thenReturn("Phone");
        when(request.getParameter("pPrice")).thenReturn("800");
        when(request.getParameter("pDiscount")).thenReturn("10");
        when(request.getParameter("pQuantity")).thenReturn("5");
        when(request.getParameter("catId")).thenReturn("3");
        when(servletContext.getRealPath("img")).thenReturn("/tmp");

        Part part = mock(Part.class);
        when(part.getSubmittedFileName()).thenReturn("pic.png");
        when(part.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));
        when(request.getPart("pPic")).thenReturn(part);

        TestableProductOperationServlet servlet = new TestableProductOperationServlet();
        servlet.processRequest(request, response);

        verify(servlet.productDao).saveProduct(org.mockito.ArgumentMatchers.any(Product.class));
        assertTrue("Expected saveFile to be called", servlet.savedFile);
        verify(session).setAttribute("message", "New product has been added successfuly");
        verify(response).sendRedirect("admin.jsp");
    }

    @Test
    public void saveFileWritesBytesToDestination() throws Exception {
        ProductOperationServlet servlet = new ProductOperationServlet();
        Part part = mock(Part.class);
        when(part.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
        java.nio.file.Path temp = java.nio.file.Files.createTempFile("product-op", ".bin");

        servlet.saveFile(part, temp.toString());

        assertEquals("data", java.nio.file.Files.readString(temp));
        java.nio.file.Files.deleteIfExists(temp);
    }

    @Test
    public void doGetDelegatesToProcessRequest() throws Exception {
        TrackingProductOperationServlet servlet = new TrackingProductOperationServlet();

        servlet.doGet(request, response);

        assertTrue(servlet.called);
    }

    @Test
    public void doPostDelegatesToProcessRequest() throws Exception {
        TrackingProductOperationServlet servlet = new TrackingProductOperationServlet();

        servlet.doPost(request, response);

        assertTrue(servlet.called);
    }

    private static class TestableProductOperationServlet extends ProductOperationServlet {
        private final CategoryDao categoryDao;
        private final ProductDao productDao;
        private boolean savedFile;

        private TestableProductOperationServlet() {
            categoryDao = mock(CategoryDao.class);
            productDao = mock(ProductDao.class);
            Category category = new Category();
            when(categoryDao.getCategoryById(anyInt())).thenReturn(category);
        }

        @Override
        protected CategoryDao createCategoryDao() {
            return categoryDao;
        }

        @Override
        protected ProductDao createProductDao() {
            return productDao;
        }

        @Override
        protected void saveFile(Part part, String path) throws IOException {
            savedFile = true;
        }
    }

    private static class TrackingProductOperationServlet extends ProductOperationServlet {
        private boolean called;

        @Override
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
            called = true;
        }
    }
}

