package com.oracle.csm.extn.datasecurity.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.oracle.csm.extn.datasecurity.domain.FndGrant;
import com.oracle.csm.extn.datasecurity.domain.FndObject;
import com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

public class FndSecurityDAO {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private static Logger logger = DSLoggerUtil.getLogger();
	
	private static SessionFactory getSessionFactory()
	{
    	Configuration configuration = new Configuration();
    	configuration.configure();
    	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
			.buildServiceRegistry();
    	sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    	return sessionFactory;
	}
	
	private static Session getSession() {
		
		Session s = null;
	
        if (sessionFactory!=null)
        { 	
        	 s = sessionFactory.openSession();   	 
        }
        else
        	s = FndSecurityDAO.getSessionFactory().openSession();
        
       return s;
	}
	    
    public List<FndObject> getFndObject(List<String> objNameList) {
    	Session s = getSession();
    	List<FndObject> resultsList = new ArrayList<FndObject>();
    	Query query=s.createQuery("from FndObject where objName in (:objName)"); 
    	query.setParameterList("objName", objNameList);
    	resultsList = query.list();
    	logger.log(Level.FINE, "Total: "+resultsList.size()+ "FndObjects are retrived from the target db!");
    	return resultsList;
    }
              
    public List<FndGrant> geFndGrants(List<Long> objIdList)
    {     
        long startTime = System.currentTimeMillis();
        Session s = getSession();
        List<FndGrant> resultsList = new <FndGrant>ArrayList();
        Query query=s.createQuery("from FndGrant g where g.fndObj.objectId in (:objectId)"); 
        query.setParameterList("objectId", objIdList);
        resultsList = query.list();
        System.out.println("Total time taken to fetch grants " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
        logger.log(Level.INFO, "Total: "+resultsList.size()+" Grants are retrived from the target db for object id:! "+objIdList.get(0));
        return resultsList;
    }
    
    public List<FndObjectInstanceSet> getFndObjectInstance(List<Long> objIdList)
    {     
        long startTime = System.currentTimeMillis();
        Session s = getSession();
        List<FndObjectInstanceSet> resultsList = new ArrayList<>();
        Query query=s.createQuery("from FndObjectInstanceSet g where g.fndObj.objectId in (:objectId)"); 
        query.setParameterList("objectId", objIdList);
        resultsList = query.list();
        System.out.println("Total time taken to fetch FndObjectInstanceSets " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
        logger.log(Level.INFO, "Total: "+resultsList.size()+" FndObjectInstanceSets are retrived from the target db for object id:! "+objIdList.get(0));
        return resultsList;
    }
    
    

}

