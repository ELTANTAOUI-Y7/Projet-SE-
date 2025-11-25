package com.mycompany.phone_shoop.helper;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryProvider {
    interface ConfigurationSupplier {
        Configuration get();
    }

    private static SessionFactory factory;
    private static final ConfigurationSupplier DEFAULT_CONFIGURATION_SUPPLIER = new ConfigurationSupplier() {
        @Override
        public Configuration get() {
            return new Configuration().configure("hibernate.cfg.xml");
        }
    };
    private static ConfigurationSupplier configurationSupplier = DEFAULT_CONFIGURATION_SUPPLIER;

    public static SessionFactory getFactory() {
        try {
            if (factory == null) {
                factory = configurationSupplier.get().buildSessionFactory();
            }
        } catch(Exception e) {
            System.err.println("Error creating Hibernate SessionFactory:");
            e.printStackTrace();
            return null;
        }
        return factory;
    }

    static void setConfigurationSupplier(ConfigurationSupplier supplier) {
        configurationSupplier = supplier == null ? DEFAULT_CONFIGURATION_SUPPLIER : supplier;
        factory = null;
    }

    static void resetFactory() {
        factory = null;
    }
}
