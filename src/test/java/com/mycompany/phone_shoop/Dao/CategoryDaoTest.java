package com.mycompany.phone_shoop.Dao;

import com.mycompany.phone_shoop.entities.Category;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryDaoTest {

    private SessionFactory factory;
    private Session session;
    private Transaction transaction;
    private CategoryDao categoryDao;

    @Before
    public void setUp() {
        factory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        when(factory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        categoryDao = new CategoryDao(factory);
    }

    @Test
    public void saveCategoryReturnsGeneratedId() {
        Category category = new Category();
        when(session.save(category)).thenReturn(15);

        int id = categoryDao.saveCategory(category);

        assertEquals(15, id);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    public void getCategoriesReturnsAllCategories() {
        Query query = mock(Query.class);
        List<Category> categories = Arrays.asList(new Category(), new Category());
        when(session.createQuery("from Category")).thenReturn(query);
        when(query.list()).thenReturn(categories);

        List<Category> result = categoryDao.getCategories();

        assertSame(categories, result);
        verify(session).close();
    }

    @Test
    public void getCategoryByIdReturnsMatchingCategory() {
        Category category = new Category();
        when(session.get(Category.class, 4)).thenReturn(category);

        Category result = categoryDao.getCategoryById(4);

        assertSame(category, result);
        verify(session).close();
    }

    @Test
    public void getCategoryByIdReturnsNullWhenExceptionThrown() {
        SessionFactory faultyFactory = mock(SessionFactory.class);
        when(faultyFactory.openSession()).thenThrow(new RuntimeException("boom"));
        CategoryDao dao = new CategoryDao(faultyFactory);

        Category result = dao.getCategoryById(99);

        assertNull(result);
    }
}

