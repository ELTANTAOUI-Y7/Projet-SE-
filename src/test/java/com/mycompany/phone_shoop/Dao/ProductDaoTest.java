package com.mycompany.phone_shoop.Dao;

import com.mycompany.phone_shoop.entities.Product;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductDaoTest {

    private SessionFactory factory;
    private Session session;
    private Transaction transaction;
    private ProductDao productDao;

    @Before
    public void setUp() {
        factory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        when(factory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        productDao = new ProductDao(factory);
    }

    @Test
    public void saveProductPersistsEntity() {
        Product product = new Product();

        boolean result = productDao.saveProduct(product);

        verify(session).save(product);
        verify(transaction).commit();
        verify(session).close();
        // method should indicate persistence succeeded
        assertEquals(true, result);
    }

    @Test
    public void saveProductReturnsFalseWhenExceptionOccurs() {
        SessionFactory faultyFactory = mock(SessionFactory.class);
        when(faultyFactory.openSession()).thenThrow(new RuntimeException("fail"));
        ProductDao dao = new ProductDao(faultyFactory);

        boolean result = dao.saveProduct(new Product());

        assertFalse(result);
    }

    @Test
    public void getAllProductsReturnsListFromQuery() {
        Query query = mock(Query.class);
        List<Product> products = Arrays.asList(new Product(), new Product(), new Product());
        when(session.createQuery("from Product")).thenReturn(query);
        when(query.list()).thenReturn(products);

        List<Product> result = productDao.getAllProducts();

        assertSame(products, result);
        verify(session).close();
    }

    @Test
    public void getAllProductsByCatIdFiltersByCategoryId() {
        Query query = mock(Query.class);
        List<Product> products = Arrays.asList(new Product());
        when(session.createQuery("from Product as p where p.category.categoryId =: id")).thenReturn(query);
        when(query.setParameter("id", 7)).thenReturn(query);
        when(query.list()).thenReturn(products);

        List<Product> result = productDao.getAllProductsByCatId(7);

        assertSame(products, result);
        verify(query).setParameter("id", 7);
        verify(session).close();
    }
}

