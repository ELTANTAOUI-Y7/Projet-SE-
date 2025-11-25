package com.mycompany.phone_shoop.helper;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FactoryProviderTest {

    @After
    public void tearDown() {
        FactoryProvider.setConfigurationSupplier(null);
        FactoryProvider.resetFactory();
    }

    @Test
    public void getFactoryUsesSupplierAndCachesInstance() {
        final Configuration configuration = mock(Configuration.class);
        final SessionFactory sessionFactory = mock(SessionFactory.class);
        when(configuration.buildSessionFactory()).thenReturn(sessionFactory);
        FactoryProvider.setConfigurationSupplier(new FactoryProvider.ConfigurationSupplier() {
            @Override
            public Configuration get() {
                return configuration;
            }
        });
        FactoryProvider.resetFactory();

        SessionFactory first = FactoryProvider.getFactory();
        SessionFactory second = FactoryProvider.getFactory();

        assertSame(sessionFactory, first);
        assertSame(first, second);
        verify(configuration, times(1)).buildSessionFactory();
    }

    @Test
    public void getFactoryReturnsNullWhenSupplierFails() {
        FactoryProvider.setConfigurationSupplier(new FactoryProvider.ConfigurationSupplier() {
            @Override
            public Configuration get() {
                throw new RuntimeException("boom");
            }
        });
        FactoryProvider.resetFactory();

        SessionFactory result = FactoryProvider.getFactory();

        assertNull(result);
    }
}

