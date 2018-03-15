package com.oracle.csm.extn.datasecurity.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.csm.extn.datasecurity.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.domain.FndFormFunction;
import com.oracle.csm.extn.datasecurity.domain.FndGrant;
import com.oracle.csm.extn.datasecurity.domain.FndObject;
import com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;

public class DSObjectsTargetProcessor {

	public static List<String> notFoundFndObjects;

	public static Map<String, Map<DataSecurityObjects, List<Object>>> extractFndObjects(
			Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap) {
		FndSecurityDAO dao = new FndSecurityDAO();
		Map<String, Map<DataSecurityObjects, List<Object>>> targetOotbObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();

		List<String> objectList = new ArrayList<String>();
		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : ootbObjectMap.entrySet()) {
			String key = entry.getKey();
			objectList.add(key);
		}

		List<FndObject> FndObjList = dao.getFndObject(objectList);

		List<Long> fndObjIdList = new ArrayList<Long>();
		for (FndObject fndObject : FndObjList) {
			fndObjIdList.add(fndObject.getObjectId());
		}

		List<FndGrant> fndGrants = dao.geFndGrants(fndObjIdList);

		for (FndObject fndObj : FndObjList) {

			Map<DataSecurityObjects, List<Object>> targetDsMap = new HashMap<DataSecurityObjects, List<Object>>();
			List<Object> grantList = new ArrayList<Object>();
			List<Object> menuList = new ArrayList<Object>();
//			List<Object> instanceSetList = new ArrayList<Object>();
			List<Object> formFunctionsList = new ArrayList<Object>();

			for (FndGrant fndGrant : fndGrants) {
				if (fndObj.getObjName().equals(fndGrant.getFndObj().getObjName())) {
					grantList.add(fndGrant);
					menuList.add(fndGrant.getFndMenu());
					for (FndFormFunction formFunction : fndGrant.getFndMenu().getFndFormFunction()) {
						formFunctionsList.add(formFunction);
					}

				}
			}
			targetDsMap.put(DataSecurityObjects.GRANTS, grantList);
			targetDsMap.put(DataSecurityObjects.MENUS, menuList);
			targetDsMap.put(DataSecurityObjects.INSTANCE_SETS, new ArrayList());
			targetDsMap.put(DataSecurityObjects.FORM_FUNCIONS, formFunctionsList);

			targetOotbObjectMap.put(fndObj.getObjName(), targetDsMap);
			

		}
		
		List<FndObjectInstanceSet> instaceSetList = dao.getFndObjectInstance(fndObjIdList);
		for (FndObjectInstanceSet fndObjectInstanceSet : instaceSetList) {
			
			targetOotbObjectMap.get(fndObjectInstanceSet.getFndObj().getObjName()).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
		}
		/*
		 * for (FndObject fndObject : FndObjList) {
		 * 
		 * List grantList = new ArrayList(); List menuList = new ArrayList(); List
		 * fromFunctionList = new ArrayList(); List fndInstanceSetList = new
		 * ArrayList();
		 * 
		 * List<Long> objIdList = new <Long>ArrayList();
		 * 
		 * objIdList.add(fndObject.getObjectId()); List<FndGrant> result =
		 * dao.geFndGrants(objIdList); for (FndGrant fndGrant : result) {
		 * 
		 * grantList.add(fndGrant); targetDs.put(DataSecurityObjects.GRANTS, grantList);
		 * 
		 * menuList.add(fndGrant.getFndMenu()); targetDs.put(DataSecurityObjects.MENUS,
		 * menuList); //fromFunctionList.add(fndGrant.get)
		 * 
		 * fromFunctionList.add(fndGrant.getFndMenu().getFndFormFunction());
		 * targetDs.put(DataSecurityObjects.FORM_FUNCIONS, fromFunctionList);
		 * 
		 * fndInstanceSetList.add(fndGrant.getInstanceSet());
		 * targetDs.put(DataSecurityObjects.INSTANCE_SETS, fndInstanceSetList);
		 * 
		 * }
		 * 
		 * targetOotbObjectMap.put(fndObject.getObjName(), targetDs); }
		 */

		return targetOotbObjectMap;
	}

}