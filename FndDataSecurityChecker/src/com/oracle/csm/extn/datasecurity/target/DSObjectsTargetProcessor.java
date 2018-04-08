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
import java.util.logging.Logger;

import com.oracle.csm.extn.datasecurity.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.domain.FndFormFunctionTarget;
import com.oracle.csm.extn.datasecurity.domain.FndGrantTarget;
import com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSetTarget;
import com.oracle.csm.extn.datasecurity.domain.FndObjectTarget;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

public class DSObjectsTargetProcessor {

	private static ExecutorService executor = Executors.newFixedThreadPool(20);
	private static Logger logger = DSLoggerUtil.getLogger();

	public static Map<String, Map<DataSecurityObjects, List<Object>>> extractFndObjects(
			Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap) {

		final FndSecurityDAO dao = new FndSecurityDAO();
		Map<String, Map<DataSecurityObjects, List<Object>>> targetOotbObjectMap = new HashMap<>();

		List<String> objectList = new ArrayList<>();
		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : ootbObjectMap.entrySet()) {
			String key = entry.getKey();
			objectList.add(key);
		}

		// Fet all objects that are present in target for the ootbObjectMap from source
		// csm
		List<FndObjectTarget> FndObjList = dao.getFndObject(objectList);

		logger.info("DSObjectsTargetProcessor/extractFndObjects:  ");
		// Fetch all fndgrants for the objects that are present in the target.
		List<Future<List<FndGrantTarget>>> fndGrantList = new ArrayList<>();
		List<Future<List<FndObjectInstanceSetTarget>>> fndInstnaceList = new ArrayList<>();
		List<Future<List<FndFormFunctionTarget>>> fndFormList = new ArrayList<>();

		List<Long> fndObjectIdList = new ArrayList<>();

		long startTime2 = System.currentTimeMillis();

		for (final FndObjectTarget fndObject : FndObjList) {

			Map<DataSecurityObjects, List<Object>> targetDsMap = new HashMap<>();

			targetDsMap.put(DataSecurityObjects.GRANTS, new ArrayList<>());
			targetDsMap.put(DataSecurityObjects.MENUS, new ArrayList<>());
			targetDsMap.put(DataSecurityObjects.INSTANCE_SETS, new ArrayList<>());
			targetDsMap.put(DataSecurityObjects.FORM_FUNCIONS, new ArrayList<>());

			targetOotbObjectMap.put(fndObject.getObjName(), targetDsMap);

			fndObjectIdList.add(fndObject.getObjectId());
			Future<List<FndGrantTarget>> result = executor.submit(new Callable<List<FndGrantTarget>>() {
				@Override
				public List<FndGrantTarget> call() throws Exception {
					return dao.getFndGrants(fndObject.getObjectId());
				}
			});
			fndGrantList.add(result);
		}

		List<FndGrantTarget> fndGrants = new ArrayList<>();

		for (@SuppressWarnings("rawtypes")
		Future future : fndGrantList) {
			try {
				fndGrants.addAll((List) future.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// System.out.println(fndGrants.size());
		System.out.println("Total time taken to fetch all grants after fetching the objects "
				+ (System.currentTimeMillis() - startTime2) / 1000 + " seconds");

		for (FndGrantTarget fndGrant : fndGrants) {
			targetOotbObjectMap.get(fndGrant.getFndObj().getObjName()).get(DataSecurityObjects.GRANTS).add(fndGrant);
			targetOotbObjectMap.get(fndGrant.getFndObj().getObjName()).get(DataSecurityObjects.MENUS)
					.add(fndGrant.getFndMenu());

			/*
			 * for (FndFormFunctionTarget formFunction :
			 * fndGrant.getFndMenu().getFndFormFunction()) {
			 * targetOotbObjectMap.get(fndGrant.getFndObj().getObjName()).get(
			 * DataSecurityObjects.FORM_FUNCIONS).add(formFunction);
			 * 
			 * }
			 */
		}

		// Get instance sets for object ids and add it to target map

		List<FndObjectInstanceSetTarget> fndObjectIdListForInstance = new ArrayList<>();

		for (final FndObjectTarget fndObject : FndObjList) {

			Future<List<FndObjectInstanceSetTarget>> result = executor
					.submit(new Callable<List<FndObjectInstanceSetTarget>>() {
						@Override
						public List<FndObjectInstanceSetTarget> call() throws Exception {
							return dao.getFndObjectInstance(fndObject.getObjectId());
						}
					});
			fndInstnaceList.add(result);

			for (@SuppressWarnings("rawtypes")
			Future future : fndInstnaceList) {
				try {
					fndObjectIdListForInstance.addAll((List) result.get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		for (FndObjectInstanceSetTarget fndObjectInstanceSet : fndObjectIdListForInstance) {

			targetOotbObjectMap.get(fndObjectInstanceSet.getFndObj().getObjName())
					.get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
		}

		// List<FndObjectInstanceSetTarget> instaceSetList =
		// dao.getFndObjectInstance(fndObjectIdList);

		// Get form functions for object ids and add it to target map

		List<FndFormFunctionTarget> fndFormFunctionList = new ArrayList<>();

		for (final FndObjectTarget fndObject : FndObjList) {

			Future<List<FndFormFunctionTarget>> result = executor.submit(new Callable<List<FndFormFunctionTarget>>() {
				@Override
				public List<FndFormFunctionTarget> call() throws Exception {
					return dao.getFndFormFunction(fndObject.getObjectId());
				}
			});
			fndFormList.add(result);

			for (@SuppressWarnings("rawtypes")
			Future future : fndFormList) {
				try {
					fndFormFunctionList.addAll((List) result.get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		for (FndFormFunctionTarget fndForm : fndFormFunctionList) {

			targetOotbObjectMap.get(fndForm.getFndObject().getObjName()).get(DataSecurityObjects.FORM_FUNCIONS)
					.add(fndForm);
		}

		executor.shutdown();

		System.out.println("Total time taken to prepare target map after fetching fnd objects "
				+ (System.currentTimeMillis() - startTime2) / 1000 + " seconds");

		return targetOotbObjectMap;
	}

}