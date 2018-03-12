package com.oracle.csm.extn.datasecurity.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.oracle.csm.extn.datasecurity.domain.FndObject;
import com.oracle.csm.extn.datasecurity.utils.HibernateUtil;

public class FndSecurityDAO {

	public List<FndObject> getFndObject(FndObject fndObj) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(FndObject.class);
		criteria.add(Restrictions.eq("objName", fndObj.getObjName()));
		List<FndObject> results = criteria.list();

		System.out.println("FndObject " + results.get(0).getObjName());
		return results;
	}

}
