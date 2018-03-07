package com.oracle.csm.extn.datasecurity.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.oracle.csm.extn.datasecurity.domain.FndObject;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;
import com.oracle.csm.extn.datasecurity.utils.HibernateUtil;

public class DaoTest {
	private static Logger logger = DSLoggerUtil.getLogger();

	public static void main(String[] args)   {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();

		Transaction transaction = null;
		try {
			transaction =  session.beginTransaction();
			List<FndObject> list = session.createCriteria(FndObject.class).list();
			System.out.println(list.get(0).getObjName());
			
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.log(Level.INFO, ex.toString());
			ex.printStackTrace(System.err);
		} finally {
			session.close();
		}

	}
}
