package com.mycompany.phone_shoop.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HelperTest {

    @Test
    public void get10WordsTruncatesLongDescriptions() {
        String desc = "one two three four five six seven eight nine ten eleven twelve";

        String result = Helper.get10Words(desc);

        assertEquals("one two three four five six seven eight nine ten ...", result);
    }

    @Test
    public void get10WordsReturnsFullDescWhenShort() {
        String desc = "small description";

        String result = Helper.get10Words(desc);

        assertEquals("small description...", result);
    }

    @Test
    public void getCountsReturnsUserAndProductTotals() {
        SessionFactory factory = mock(SessionFactory.class);
        Session session = mock(Session.class);
        Query userQuery = mock(Query.class);
        Query productQuery = mock(Query.class);
        when(factory.openSession()).thenReturn(session);
        when(session.createQuery("Select count(*) from User")).thenReturn(userQuery);
        when(session.createQuery("Select count(*) from Product")).thenReturn(productQuery);
        when(userQuery.list()).thenReturn(Arrays.asList(5L));
        when(productQuery.list()).thenReturn(Arrays.asList(8L));

        Map<String, Long> result = Helper.getCounts(factory);

        Map<String, Long> expected = new HashMap<>();
        expected.put("userCount", 5L);
        expected.put("productCount", 8L);
        assertEquals(expected, result);
        verify(session).close();
    }
}

