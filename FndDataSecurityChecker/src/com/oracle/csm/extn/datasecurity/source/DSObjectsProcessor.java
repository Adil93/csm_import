package com.oracle.csm.extn.datasecurity.source;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndFormFunction;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.model.FndMenu;
import com.oracle.csm.extn.datasecurity.model.FndObject;
import com.oracle.csm.extn.datasecurity.model.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.model.SeedData;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

public class DSObjectsProcessor {

	private static Logger logger = DSLoggerUtil.getLogger();

	public static List<FndObject> wholeFndObjects = new ArrayList<FndObject>();
	public static List<FndGrant> wholeFndGrants = new ArrayList<FndGrant>();
	public static List<FndFormFunction> wholeFndFormFunctions = new ArrayList<FndFormFunction>();
	public static List<FndMenu> wholeFndMenus = new ArrayList<FndMenu>();

	// split the map
	// OOTb and associatd data
	// Self scanning for custom objects - self scan
	// if instance set there should be object
	// Fnd
	// make sure fnd grant menu present in fnd menu
	private static Map<String, Map<DataSecurityObjects, List<Object>>> objectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();

	private static Map<String, Map<DataSecurityObjects, List<Object>>> OOTBObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();
	private static Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();

	private static Map<String, FndMenu> menuNameMap = new HashMap<String, FndMenu>();
	private static Map<String, FndObject> objectNameMap = new HashMap<String, FndObject>();

	public DSObjectsProcessor() {
		// TODO Auto-generated constructor stub
	}

	protected static void proccessXmls(ByteArrayInputStream byteArrayInputStream, String name) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(SeedData.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			SeedData seedData = (SeedData) unmarshaller.unmarshal(byteArrayInputStream);

			if (name.contains("FndObjectsSD.xml")) {
				if (seedData.getFndObjects() != null) {
					processFNDObjectsXML(seedData.getFndObjects(), name);
				}
			} else if (name.contains("FndGrantsSD.xml")) {
				if (seedData.getFndGrants() != null)
					processFNDGrantsXML(seedData.getFndGrants(), name);
			} else if (name.contains("FndFormFunctionsSD.xml")) {
				if (seedData.getFndFormFunctions() != null)
					processFNDFormFunctionXML(seedData.getFndFormFunctions(), name);
			} else if (name.contains("FndMenusSD.xml")) {
				if (seedData.getFndMenus() != null)
					processFNDMenusXML(seedData.getFndMenus(), name);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	private static void processFNDObjectsXML(List<FndObject> fndObjects, String fileName) {
		if (fndObjects != null) {
			logger.log(Level.INFO, "Processing file " + fileName);

			for (FndObject fndObject : fndObjects) {
				wholeFndObjects.add(fndObject);
				objectNameMap.put(fndObject.getObjName(), fndObject);
			}

		}

	}

	private static void processFNDGrantsXML(List<FndGrant> fndGrants, String fileName) {
		if (fndGrants != null) {
			logger.log(Level.INFO, "Processing file " + fileName);

			for (FndGrant fndGrant : fndGrants) {
				wholeFndGrants.add(fndGrant);

			}
		}
	}

	private static void processFNDFormFunctionXML(List<FndFormFunction> fndFormFunctions, String fileName) {
		if (fndFormFunctions != null) {
			logger.log(Level.INFO, "Processing file " + fileName);

			for (FndFormFunction fndFormFunction : fndFormFunctions) {
				wholeFndFormFunctions.add(fndFormFunction);
			}
		}
	}

	private static void processFNDMenusXML(List<FndMenu> fndMenus, String fileName) {
		if (fndMenus != null) {
			logger.log(Level.INFO, "Processing file " + fileName);

			for (FndMenu fndMenu : fndMenus) {
				wholeFndMenus.add(fndMenu);

				if (!menuNameMap.containsKey(fndMenu.getMenuName())) {
					menuNameMap.put(fndMenu.getMenuName(), fndMenu);
				}
			}
		}
	}

	public static Map<String, Map<DataSecurityObjects, List<Object>>> proccessMaps() {
		try {
			logger.log(Level.INFO, "Processing Object Map ");
			for (FndObject fndObject : wholeFndObjects) {
				String objName = fndObject.getObjName();

				if (!objectMap.containsKey(objName)) {
					objectMap.put(objName, new HashMap<DataSecurityObjects, List<Object>>());
					initializeSeedDataMap(objectMap.get(objName));

				}

				if (fndObject.getFndObjectInstanceSets() != null)
					for (FndObjectInstanceSet fndObjectInstanceSet : fndObject.getFndObjectInstanceSets()) {
						objectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
					}
			}

			for (FndGrant fndGrant : wholeFndGrants) {
				String objName = fndGrant.getObjName();

				if (objectMap.containsKey(objName)) {
					objectMap.get(objName).get(DataSecurityObjects.GRANTS).add(fndGrant);
					if (fndGrant.getMenuName() != null) {
						if (menuNameMap.get(fndGrant.getMenuName()) != null)
							objectMap.get(objName).get(DataSecurityObjects.MENUS)
									.add(menuNameMap.get(fndGrant.getMenuName()));
						else {
							FndMenu fndMenu = new FndMenu(fndGrant.getMenuName());
							objectMap.get(objName).get(DataSecurityObjects.MENUS).add(fndMenu);
						}
					}

					if (fndGrant.getInstanceSetName() != null) {
						boolean found = false;

						if (objectNameMap.containsKey(objName) && objectNameMap.get(objName) != null) {
							if (objectNameMap.get(objName).getFndObjectInstanceSets() != null
									&& objectNameMap.get(objName).getFndObjectInstanceSets().size() > 0) {

								for (FndObjectInstanceSet fndObjectInstanceSet : objectNameMap.get(objName)
										.getFndObjectInstanceSets()) {
									if (fndGrant.getInstanceSetName()
											.equals(fndObjectInstanceSet.getInstanceSetName())) {
										found = true;
										break;
									}
								}
							}
						}
						if (!found) {
							FndObjectInstanceSet fndObjectInstanceSet = new FndObjectInstanceSet(
									fndGrant.getInstanceSetName());
							objectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
						}

					}

				} else {
					objectNameMap.put(fndGrant.getObjName(), null);
					objectMap.put(objName, new HashMap<DataSecurityObjects, List<Object>>());
					initializeSeedDataMap(objectMap.get(objName));
					objectMap.get(objName).get(DataSecurityObjects.GRANTS).add(fndGrant);

					FndObjectInstanceSet fndObjectInstanceSet = new FndObjectInstanceSet(fndGrant.getInstanceSetName());
					objectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
				}
			}

			for (FndFormFunction fndFormFunction : wholeFndFormFunctions) {

				String objName = fndFormFunction.getObjectName();

				if (objectMap.containsKey(objName)) {
					objectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS).add(fndFormFunction);

				} else {
					objectNameMap.put(fndFormFunction.getObjectName(), null);
					objectMap.put(objName, new HashMap<DataSecurityObjects, List<Object>>());
					initializeSeedDataMap(objectMap.get(objName));
					objectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS).add(fndFormFunction);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectMap;

	}

	private static void initializeSeedDataMap(Map<DataSecurityObjects, List<Object>> objectSeedDataMap) {
		objectSeedDataMap.put(DataSecurityObjects.GRANTS, new ArrayList<Object>());
		objectSeedDataMap.put(DataSecurityObjects.FORM_FUNCIONS, new ArrayList<Object>());
		objectSeedDataMap.put(DataSecurityObjects.MENUS, new ArrayList<Object>());
		objectSeedDataMap.put(DataSecurityObjects.INSTANCE_SETS, new ArrayList<Object>());
	}

}
