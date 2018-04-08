package com.oracle.csm.extn.datasecurity.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * 
 * @author adilmuthukoya
 *
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.configure();
		configuration.setProperty("hibernate.connection.url",
				ConfigurationReaderUtil.getConfigurationProperty("hibernate.connection.url"));
		configuration.setProperty("hibernate.connection.username",
				ConfigurationReaderUtil.getConfigurationProperty("hibernate.connection.username"));
		configuration.setProperty("hibernate.connection.password",
				ConfigurationReaderUtil.getConfigurationProperty("hibernate.connection.password"));

		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}

	public static Session getSession() {

		Session s = null;

		if (sessionFactory != null) {
			s = sessionFactory.openSession();
		} else
			s = getSessionFactory().openSession();

		return s;
	}
}