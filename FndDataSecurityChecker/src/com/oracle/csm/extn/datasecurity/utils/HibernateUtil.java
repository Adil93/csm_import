package com.oracle.csm.extn.datasecurity.utils;

import javax.imageio.spi.ServiceRegistry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

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

		StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
				.applySettings(configuration.getProperties()).build();
		Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
		sessionFactory = metaData.getSessionFactoryBuilder().build();

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