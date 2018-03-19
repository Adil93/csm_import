package com.oracle.csm.extn.dao;

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
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;
import com.oracle.csm.extn.domain.FndGrantTarget;
import com.oracle.csm.extn.domain.FndObjectInstanceSetTarget;
import com.oracle.csm.extn.domain.FndObjectTarget;

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
	    
    public List<FndObjectTarget> getFndObject(List<String> objNameList) {
    	Session s = getSession();
    	List<FndObjectTarget> resultsList = new ArrayList<>();
    	Query query=s.createQuery("from FndObjectTarget where objName in (:objName)"); 
    	query.setParameterList("objName", objNameList);
    	resultsList = query.list();
    	logger.log(Level.FINE, "Total: "+resultsList.size()+ "FndObjects are retrived from the target db!");
    	return resultsList;
    }
              
/*    public List<FndGrantTarget> geFndGrants(List<Long> objIdList)
    { 	
		long startTime = System.currentTimeMillis();
    	Session s = getSession();
    	List<FndGrantTarget> resultsList = new <FndGrantTarget>ArrayList();
    	Query query=s.createQuery("from FndGrantTarget g where g.fndObj.objectId in (:objectId)"); 
    	query.setParameterList("objectId", objIdList);
    	resultsList = query.list();
    	System.out.println("Total time taken to fetch grants " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
    	logger.log(Level.FINE, "Total: "+resultsList.size()+" Grants are retrived from the target db for object id:! "+objIdList.get(0));
    	return resultsList;
    }
    */
    
    public List<FndGrantTarget> geFndGrants(Long objId)
    { 	//TODO change to return a single FndGrantTarget instead of a list
    	List<Long> objList = new ArrayList<>();
    	objList.add(objId);
    	long startTime = System.currentTimeMillis();
    	Session s = getSession();
    	List<FndGrantTarget> resultsList = new ArrayList<>();
    	Query query=s.createQuery("from FndGrantTarget g where g.fndObj.objectId in (:objectId)"); 
    	query.setParameterList("objectId", objList);
    	resultsList = query.list();
    	System.out.println("Total time taken to fetch grants " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
    	logger.log(Level.INFO, "Total: "+resultsList.size()+" Grants are retrived from the target db for object id:!");
    	return resultsList;
    }
    //TODO: Taking too much time to fetch all grants for 507 objects
      
    public List<FndObjectInstanceSetTarget> getFndObjectInstance(Long objId)
    {   
    	List<Long> objList = new ArrayList<>();
    	objList.add(objId);
        long startTime = System.currentTimeMillis();
        Session s = getSession();
        List<FndObjectInstanceSetTarget> resultsList = new ArrayList<>();
        Query query=s.createQuery("from FndObjectInstanceSetTarget g where g.fndObj.objectId in (:objectId)"); 
        query.setParameterList("objectId", objList);
        resultsList = query.list();
        System.out.println("Total time taken to fetch FndObjectInstanceSets " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
        logger.log(Level.INFO, "Total: "+resultsList.size()+" FndObjectInstanceSets are retrived from the target db for object id:!");
        return resultsList;
    } 

}

