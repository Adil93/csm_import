package com.oracle.csm.extn.datasecurity.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.*;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.oracle.csm.extn.datasecurity.domain.FndObject;

public class FndSecurityDAO {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	private static Session getSession() {
		Session s = null;
        if(s == null)
        { 	
        	Configuration configuration = new Configuration();
        	configuration.configure();
        	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
        	sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        	s = sessionFactory.openSession();
        	return s;
        }
        else
        	return s;
	}
	    
    public List<FndObject> getFndObject(FndObject fndObj) {
    	Session s = getSession();
    	Criteria cr = s.createCriteria(FndObject.class);
    	cr.add(Restrictions.eq("objName", fndObj.getObjName()));   
    	List<FndObject> results = cr.list();

       //	FndObject fndObject= (FndObject) s.get(FndObject.class, fndObj.getObjectId());

    	System.out.println("FndObject "+results.get(0).getObjName());
    	return results;
    }
    

}

