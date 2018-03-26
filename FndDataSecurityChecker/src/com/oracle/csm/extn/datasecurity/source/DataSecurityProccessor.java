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

	private static Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();
	private static Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();

	private static Map<String, FndMenu> menuNameMap = new HashMap<String, FndMenu>();
	private static Map<String, FndObject> objectNameMap = new HashMap<String, FndObject>();
	private static Map<String, FndObjectInstanceSet> instanceSetNameMap = new HashMap<String, FndObjectInstanceSet>();
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

								}
							 else {
								FndMenu fndMenu = new FndMenu(fndGrant.getMenuName());
								fndMenu.setCreatedBy("SEED_DATA_FROM_APPLICATION");
								objectMap.get(objName).get(DataSecurityObjects.MENUS).add(fndMenu);
								if(missingMenuMap.containsKey(objName))
								{
									missingMenuMap.get(objName).add(fndMenu);
								}
								else {
									missingMenuMap.put(objName, new ArrayList<>());
									missingMenuMap.get(objName).add(fndMenu);
								}
								
							}}

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

	public static boolean validateSourceData(Map<String, Map<DataSecurityObjects, List<Object>>> customObjectMap) {
		// Write logics for Source data validations
		logger.log(Level.INFO, "Validating Source CSM data........[Mainly Custom Object validation]");

		// Make two separate threads and run zmmNotesValidation and custom validation in
		// parallel
		// Check whether we need to use Executer service to do it here

		final boolean[] result = new boolean[2];

		Thread zmmThread = new Thread(() -> {
			result[0] = zmmNotesValidation();

		}, "ZMMNOTES_THREAD");

		Thread customObjectThread = new Thread(() -> {
			result[1] = customObjectValidation();
		}, "CUSTOM_OBJECTS_THREAD");

		zmmThread.start();
		customObjectThread.start();

		try {
			zmmThread.join();
			customObjectThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// result[0] = zmmNotesValidation();
		// result[1] = customObjectValidation();
		return result[0] && result[1];

	}

	private static boolean customObjectValidation() {
		// to be changed
		logger.log(Level.INFO, "Validating CustomObjects instacne set and menu dependencies");

		boolean customObjectValid = true;

		Set<String> validGrantNames = new HashSet<String>();
		Set<String> invalidGrantNames = new HashSet<String>();

		List<FndGrant> fndGrants = null;

		int objCount = customObjectMap.keySet().size();
		logger.log(Level.INFO, "Total number of custom Objects : " + objCount);

		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : customObjectMap.entrySet()) {

			int grantSize = entry.getValue().get(DataSecurityObjects.GRANTS).size();

			logger.log(Level.FINE, "Object : " + entry.getKey() + " has " + grantSize + " grants");

			fndGrants = entry.getValue().get(DataSecurityObjects.GRANTS).stream().map(obj -> (FndGrant) obj)
					.collect(Collectors.toList());

			// put a warning here instead of considering as invalid

			for (FndGrant fndGrant : fndGrants) {

				String instanceSetName = fndGrant.getInstanceSetName();
				String menuName = fndGrant.getMenuName();

				if (instanceSetName != null && !instanceSetName.equals("")) {
					if (!(instanceSetNameMap.containsKey(instanceSetName) && menuNameMap.containsKey(menuName))) {

						customObjectValid = false;
						// break;
						invalidGrantNames.add(fndGrant.getGrantGuid());
					} else {
						validGrantNames.add(fndGrant.getGrantGuid());
					}
				} else {
					if (!menuNameMap.containsKey(menuName)) {
						customObjectValid = false;
						// break;
						invalidGrantNames.add(fndGrant.getGrantGuid());
					} else {
						validGrantNames.add(fndGrant.getGrantGuid());
					}
				}
			}

		}
		logger.log(Level.INFO, "Invalid Grant Object size : " + invalidGrantNames.size());
		logger.log(Level.INFO, "valid Grant Object size : " + validGrantNames.size());
		logger.log(Level.INFO,
				"Invalid Grant Object Names [Cutom Object Validation] : " + invalidGrantNames.toString());
		// logger.log(Level.INFO, "Valid Grant Object Names [Custom Object Validation] :
		// " + validGrantNames.toString());

		return customObjectValid;
	}

	private static boolean zmmNotesValidation() {

		logger.log(Level.INFO, "Validating ZMM_NOTES dependenciess");
		boolean zmmValid = true;
		List<FndGrant> fndGrants = null;

		if (zmmAppCompNotesGrants != null) {
			fndGrants = zmmAppCompNotesGrants;
		} else {
			logger.log(Level.INFO, "No ZMM/AppCmmnCompNotes/FndGrantsSD.xml exists");
			return false;
		}

		Set<String> validCustNames = new HashSet<String>();
		Set<String> InValidCustNames = new HashSet<String>();

		for (Map.Entry<String, Map<DataSecurityObjects, List<Object>>> entry : customObjectMap.entrySet()) {
			boolean foundRelatedFndGrant = false;
			boolean foundRelMenuAndIS = false;
			String instanceSetName;
			String menuName;
			String custObjName = entry.getKey();

			for (Object obj : fndGrants) {
				FndGrant fndGrant = (FndGrant) obj;
				if (fndGrant.getName().toLowerCase().contains(custObjName.toLowerCase())) {

					foundRelatedFndGrant = true;

					instanceSetName = fndGrant.getInstanceSetName();
					menuName = fndGrant.getMenuName();

					if (instanceSetName != null && !instanceSetName.equals("")) {
						if (instanceSetNameMap.containsKey(instanceSetName) && menuNameMap.containsKey(menuName)) {
							foundRelMenuAndIS = true;
							// validCustNames.add(custObjName);
						}
					} else {
						if (menuNameMap.containsKey(menuName)) {
							foundRelMenuAndIS = true;
							// validCustNames.add(custObjName);
						}
					}

					break;
				}
			}

			if (!(foundRelatedFndGrant && foundRelMenuAndIS)) {
				// return false;
				InValidCustNames.add(custObjName);
				zmmValid = false;

			} else {
				validCustNames.add(custObjName);
			}

		}
		logger.log(Level.INFO, "Invalid Custom Object size : " + InValidCustNames.size());
		logger.log(Level.INFO, "valid Custom Object size : " + validCustNames.size());
		logger.log(Level.INFO, "Invalid Custom Object Names [ZMM Validation] : " + InValidCustNames.toString());
		logger.log(Level.INFO, "Valid Custom Object Names [ZMM Validation]  : " + validCustNames.toString());

		return zmmValid;
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
