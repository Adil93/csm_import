package com.oracle.csm.extn.datasecurity.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static Configuration cfg;
	private static ServiceRegistry serviceRegistry;
	static {
		try {
			cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
			sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}