package com.oracle.csm.extn.datasecurity.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;
import com.oracle.csm.extn.datasecurity.utils.HibernateUtil;
import com.oracle.csm.extn.datasecurity.domain.FndGrantTarget;
import com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSetTarget;
import com.oracle.csm.extn.datasecurity.domain.FndObjectTarget;

public class FndSecurityDAO {

	private static Logger logger = DSLoggerUtil.getLogger();

	public List<FndObjectTarget> getFndObject(List<String> objNameList) {
		Session s = HibernateUtil.getSession();
		List<FndObjectTarget> resultsList = new ArrayList<>();
		Query query = s.createQuery("from FndObjectTarget where objName in (:objName)");
		query.setParameterList("objName", objNameList);
		resultsList = query.list();
		logger.log(Level.FINE, "Total: " + resultsList.size() + "FndObjects are retrived from the target db!");
		return resultsList;
	}

	public List<FndGrantTarget> getFndGrants(Long objId) { // TODO change to return a single FndGrantTarget instead of a
															// list

		long startTime = System.currentTimeMillis();
		Session s = HibernateUtil.getSession();
		List<FndGrantTarget> result = (List) s.createCriteria(FndGrantTarget.class).createAlias("fndObj", "o")
				.add(Restrictions.eq("o.objectId", objId)).list();

		logger.log(Level.INFO, "Total: " + result.size() + " Grants are retrived from the target db for object id: "
				+ objId + " --  In " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
		return result;
	}

	public List<FndObjectInstanceSetTarget> getFndObjectInstance(Long objId) {
		long startTime = System.currentTimeMillis();
		Session s = HibernateUtil.getSession();
		List<FndObjectInstanceSetTarget> resultsList = new ArrayList<>();
		resultsList = (List) s.createCriteria(FndObjectInstanceSetTarget.class).createAlias("fndObj", "o")
				.add(Restrictions.eq("o.objectId", objId)).list();

		logger.log(Level.INFO,
				"Total: " + resultsList.size()
						+ " FndObjectInstanceSets are retrived from the target db for object id:!" + objId + " --  In "
						+ (System.currentTimeMillis() - startTime) / 1000 + " seconds");
		return resultsList;
	}

}
