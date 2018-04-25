package com.oracle.csm.extn.datasecurity.source;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndFormFunction;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.model.FndMenu;
import com.oracle.csm.extn.datasecurity.model.FndMenuEntries;
import com.oracle.csm.extn.datasecurity.model.FndObject;
import com.oracle.csm.extn.datasecurity.model.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.model.SeedData;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

/**
 * 
 * @author adilmuthukoya
 *
 */
public class DataSecurityProccessor {

	private static Logger logger = DSLoggerUtil.getLogger();

	private static List<FndObject> wholeFndObjects = new ArrayList<FndObject>();
	private static List<FndGrant> wholeFndGrants = new ArrayList<FndGrant>();
	private static List<FndFormFunction> wholeFndFormFunctions = new ArrayList<FndFormFunction>();
	private static List<FndMenu> wholeFndMenus = new ArrayList<FndMenu>();
	private static List<FndGrant> zmmAppCompNotesGrants = null;

	public static List<FndGrant> getZmmAppCompNotesGrants() {
		return zmmAppCompNotesGrants;
	}

	private static Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();
	private static Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();

	private static Map<String, FndMenu> menuNameMap = new HashMap<String, FndMenu>();
	private static Map<String, FndObject> objectNameMap = new HashMap<String, FndObject>();
	private static Map<String, FndObjectInstanceSet> instanceSetNameMap = new HashMap<String, FndObjectInstanceSet>();
	private static Map<String, FndFormFunction> functionNameMap = new HashMap<String, FndFormFunction>();

	public static Map<String, FndMenu> getMenuNameMap() {
		return menuNameMap;
	}

	public static Map<String, FndObjectInstanceSet> getInstanceSetNameMap() {
		return instanceSetNameMap;
	}

	private static Set<String> fndObjectInstacneSetNames = new HashSet<>();
	private static Set<String> fndMenuNames = new HashSet<>();
	private static Map<String, List<Object>> missingInstacneSetMap = new HashMap<>();
	private static Map<String, List<Object>> missingMenuMap = new HashMap<String, List<Object>>();

	public static Map<String, List<Object>> getMissingMenuMap() {
		return missingMenuMap;
	}

	public DataSecurityProccessor() {
		// TODO Auto-generated constructor stub
	}

	protected static void proccessXml(ByteArrayInputStream byteArrayInputStream, String name) {
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
			logger.log(Level.INFO, name);
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

			if (fileName.contains("ZMM/AppCmmnCompNotes/FndGrantsSD.xml")) {
				zmmAppCompNotesGrants = fndGrants;
			}

			// want to add here as well

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
				functionNameMap.put(fndFormFunction.getFunctionName() + ">" + fndFormFunction.getObjectName(),
						fndFormFunction);
			}
		}
	}

	private static void processFNDMenusXML(List<FndMenu> fndMenus, String fileName) {
		if (fndMenus != null) {
			logger.log(Level.INFO, "Processing file " + fileName);

			for (FndMenu fndMenu : fndMenus) {
				wholeFndMenus.add(fndMenu);
				/**for (FndMenuEntries fndMenuEntries : fndMenu.getFndMenuEntries()) {
					if (!functionNameMap
							.containsKey(fndMenuEntries.getFormFunction() + ">" + fndMenuEntries.getObjName())) {
						FndFormFunction fndFormFunction = new FndFormFunction();
						fndFormFunction.setFunctionName(fndMenuEntries.getFormFunction());
						fndFormFunction.setObjectName(fndMenuEntries.getObjName());
						fndFormFunction.setCreatedBy("CUSTOMIZED_USER");
						wholeFndFormFunctions.add(fndFormFunction);
						functionNameMap.put(fndMenuEntries.getFormFunction() + ">" + fndMenuEntries.getObjName(),
								fndFormFunction);
					}
				}**/
				if (!menuNameMap.containsKey(fndMenu.getMenuName())) {
					menuNameMap.put(fndMenu.getMenuName(), fndMenu);
				}
			}
		}
	}

	public static void proccessMaps() {
		try {
			logger.log(Level.FINE, "Processing Object Map ");
			Map<String, Map<DataSecurityObjects, List<Object>>> objectMap;

			logger.log(Level.FINE, "Processing All the FndObjects");
			for (FndObject fndObject : wholeFndObjects) {

				String objName = fndObject.getObjName();

				objectMap = getObjectSpecificMap(objName);

				if (!objectMap.containsKey(objName)) {
					objectMap.put(objName, new HashMap<DataSecurityObjects, List<Object>>());
					initializeSeedDataMap(objectMap.get(objName));

				}

				if (fndObject.getFndObjectInstanceSets() != null)
					for (FndObjectInstanceSet fndObjectInstanceSet : fndObject.getFndObjectInstanceSets()) {
						objectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
						fndObjectInstacneSetNames.add(fndObjectInstanceSet.getInstanceSetName());
						instanceSetNameMap.put(fndObjectInstanceSet.getInstanceSetName(), fndObjectInstanceSet);
					}

			}

			logger.log(Level.FINE, "Processing All the FndGrants and related Menus");
			for (FndGrant fndGrant : wholeFndGrants) {
				String objName = fndGrant.getObjName();

				objectMap = getObjectSpecificMap(objName);

				if (objectMap.containsKey(objName)) {
					objectMap.get(objName).get(DataSecurityObjects.GRANTS).add(fndGrant);

					if (fndGrant.getMenuName() != null && !"".equals(fndGrant.getMenuName())) {

						if (!fndMenuNames.contains(fndGrant.getMenuName())) {
							if (menuNameMap.containsKey(fndGrant.getMenuName())) {
								if (menuNameMap.get(fndGrant.getMenuName()) != null) {
									objectMap.get(objName).get(DataSecurityObjects.MENUS)
											.add(menuNameMap.get(fndGrant.getMenuName()));
									// for()

								} else {
									FndMenu fndMenu = new FndMenu(fndGrant.getMenuName());
									fndMenu.setCreatedBy("SEED_DATA_FROM_APPLICATION");
									objectMap.get(objName).get(DataSecurityObjects.MENUS).add(fndMenu);
									if (missingMenuMap.containsKey(objName)) {
										missingMenuMap.get(objName).add(fndMenu);
									} else {
										missingMenuMap.put(objName, new ArrayList<>());
										missingMenuMap.get(objName).add(fndMenu);
									}

								}
							}

							fndMenuNames.add(fndGrant.getMenuName());
						}
					}

					if (fndGrant.getInstanceSetName() != null && !"".equals(fndGrant.getInstanceSetName())) {
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
						if (!found && !fndObjectInstacneSetNames.contains(fndGrant.getInstanceSetName())) {
							FndObjectInstanceSet fndObjectInstanceSet = new FndObjectInstanceSet(
									fndGrant.getInstanceSetName());
							fndObjectInstanceSet.setCreatedBy("SEED_DATA_FROM_APPLICATION");
							objectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS).add(fndObjectInstanceSet);
							fndObjectInstacneSetNames.add(fndGrant.getInstanceSetName());
							if (!objName.endsWith("_c")) {
								if (missingInstacneSetMap.containsKey(objName)) {
									missingInstacneSetMap.get(objName).add(fndObjectInstanceSet);
								} else {
									missingInstacneSetMap.put(objName, new ArrayList<>());
									missingInstacneSetMap.get(objName).add(fndObjectInstanceSet);
								}
							}
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
			for(FndMenu fndMenu : wholeFndMenus) {
				for (FndMenuEntries fndMenuEntries : fndMenu.getFndMenuEntries()) {
					if (!functionNameMap
							.containsKey(fndMenuEntries.getFormFunction() + ">" + fndMenuEntries.getObjName())) {
						FndFormFunction fndFormFunction = new FndFormFunction();
						fndFormFunction.setFunctionName(fndMenuEntries.getFormFunction());
						fndFormFunction.setObjectName(fndMenuEntries.getObjName());
						fndFormFunction.setCreatedBy("CUSTOMIZED_USER");
						wholeFndFormFunctions.add(fndFormFunction);
						functionNameMap.put(fndMenuEntries.getFormFunction() + ">" + fndMenuEntries.getObjName(),
								fndFormFunction);
					}
				}
			}
			logger.log(Level.FINE, "Processing All the FndFormFunctions");
			for (FndFormFunction fndFormFunction : wholeFndFormFunctions) {

				String objName = fndFormFunction.getObjectName();

				objectMap = getObjectSpecificMap(objName);

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

	}

	private static void initializeSeedDataMap(Map<DataSecurityObjects, List<Object>> objectSeedDataMap) {
		objectSeedDataMap.put(DataSecurityObjects.GRANTS, new ArrayList<Object>());
		objectSeedDataMap.put(DataSecurityObjects.FORM_FUNCIONS, new ArrayList<Object>());
		objectSeedDataMap.put(DataSecurityObjects.MENUS, new ArrayList<Object>());
		objectSeedDataMap.put(DataSecurityObjects.INSTANCE_SETS, new ArrayList<Object>());
	}

	private static Map<String, Map<DataSecurityObjects, List<Object>>> getObjectSpecificMap(String objName) {
		if (objName.endsWith("_c")) {
			return customObjectMap;
		} else {
			return ootbObjectMap;
		}

	}

	public static Map<String, Map<DataSecurityObjects, List<Object>>> getOotbObjectMap() {
		return ootbObjectMap;
	}

	public static Map<String, Map<DataSecurityObjects, List<Object>>> getCustomObjectMap() {
		return customObjectMap;
	}

	public static List<FndObject> getWholeFndObjects() {
		return wholeFndObjects;
	}

	public static List<FndGrant> getWholeFndGrants() {
		return wholeFndGrants;
	}

	public static List<FndFormFunction> getWholeFndFormFunctions() {
		return wholeFndFormFunctions;
	}

	public static List<FndMenu> getWholeFndMenus() {
		return wholeFndMenus;
	}

	public static Map<String, List<Object>> getMissingInstacneSetMap() {
		return missingInstacneSetMap;
	}

}
