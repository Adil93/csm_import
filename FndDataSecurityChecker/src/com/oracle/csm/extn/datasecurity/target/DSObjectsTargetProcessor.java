package com.oracle.csm.extn.datasecurity.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.oracle.csm.extn.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.domain.FndFormFunctionTarget;
import com.oracle.csm.extn.domain.FndGrantTarget;
import com.oracle.csm.extn.domain.FndObjectInstanceSetTarget;
import com.oracle.csm.extn.domain.FndObjectTarget;

public class DSObjectsTargetProcessor {

	public static List<String> notFoundFndObjects;
	private static ExecutorService executor = Executors.newFixedThreadPool(20);

	public static Map<String, Map<DataSecurityObjects, List<Object>>> extractFndObjects(
			Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap) {
		final FndSecurityDAO dao = new FndSecurityDAO();
		Map<String, Map<DataSecurityObjects, List<Object>>> targetOotbObjectMap = new HashMap<>();

		List<String> objectList = new ArrayList<>();
		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : ootbObjectMap.entrySet()) {
			String key = entry.getKey();
			objectList.add(key);
		}

		List<FndObjectTarget> FndObjList = dao.getFndObject(objectList);

		List<Future<List<FndGrantTarget>>> fndGrantList = new ArrayList<>();
		List<Long> fndObjectIdList = new ArrayList<>();
		
		long startTime2 = System.currentTimeMillis();
		
		for (final FndObjectTarget fndObject : FndObjList) {
			fndObjectIdList.add(fndObject.getObjectId());
			Future<List<FndGrantTarget>> result = executor.submit(new Callable<List<FndGrantTarget>>() {
				@Override
				public List<FndGrantTarget> call() throws Exception {
					return dao.geFndGrants(fndObject.getObjectId());
				}
			});
			fndGrantList.add(result);
		}
				
		List<FndGrantTarget> fndGrants = new ArrayList<>();

		for (@SuppressWarnings("rawtypes")
		Future future : fndGrantList) {
			try {
				fndGrants.addAll((List)future.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	//	System.out.println(fndGrants.size());
		System.out.println("Total time taken to fetch all grants after fetching the objects " + (System.currentTimeMillis() - startTime2) / 1000 + " seconds");

		for (FndObjectTarget fndObj : FndObjList) {

			Map<DataSecurityObjects, List<Object>> targetDsMap = new HashMap<>();
			List<Object> grantList = new ArrayList<>();
			List<Object> menuList = new ArrayList<>();
			List<Object> formFunctionsList = new ArrayList<>();

			for (FndGrantTarget fndGrant : fndGrants) {
				if (fndObj.getObjName().equals(fndGrant.getFndObj().getObjName())) {
					grantList.add(fndGrant);
					menuList.add(fndGrant.getFndMenu());
					for (FndFormFunctionTarget formFunction : fndGrant.getFndMenu().getFndFormFunction()) {
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
		
		List<FndObjectInstanceSetTarget> fndObjectIdListForInstance = new ArrayList<>();
		
		for (final FndObjectTarget fndObject : FndObjList) {
			
			Future<List<FndObjectInstanceSetTarget>> result = executor.submit(new Callable<List<FndObjectInstanceSetTarget>>() {
				@Override
				public List<FndObjectInstanceSetTarget> call() throws Exception {
					return dao.getFndObjectInstance(fndObject.getObjectId());
				}
			});
			
			for (@SuppressWarnings("rawtypes")
			Future future : fndGrantList) {
				try {
					fndObjectIdListForInstance.addAll((List)result.get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		
		for (FndObjectInstanceSetTarget fndObjectInstanceSet : fndObjectIdListForInstance) {
			
			targetOotbObjectMap.get(fndObjectInstanceSet.getFndObj().getObjName()).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
		}
		
		
	//	List<FndObjectInstanceSetTarget> instaceSetList = dao.getFndObjectInstance(fndObjectIdList);
		
		
		
		System.out.println("Total time taken to prepare target map after fetching fnd objects " + (System.currentTimeMillis() - startTime2) / 1000 + " seconds");

		return targetOotbObjectMap;
	}

}