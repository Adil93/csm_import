package com.oracle.csm.extn.datasecurity.target;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.oracle.csm.extn.datasecurity.domain.FndFormFunctionTarget;
import com.oracle.csm.extn.datasecurity.domain.FndGrantTarget;
import com.oracle.csm.extn.datasecurity.domain.FndMenuTarget;
import com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSetTarget;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndFormFunction;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.model.FndMenu;
import com.oracle.csm.extn.datasecurity.model.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;

/**
 * 
 * @author adilmuthukoya
 *
 */
public class TargetValidator {
	private static java.util.logging.Logger logger = DSLoggerUtil.getLogger();

	public static  String fndGrantSqlfile = "/sqls/fndGrants.sql";
	public static  String fndInstanceSetsSqlfile = "/sqls/fndInstanceSets.sql";
	public static  String fndFormFunctionSqlfile = "/sqls/fndFormFunctions.sql";
	public static  String fndMenusSqlfile = "/sqls/fndMenus.sql";

	// Translation depended files
	public static  String fndInstanceSetsTLSqlfile = "/sqls/fndInstanceSetsTL.sql";
	public static  String fndFormFunctionTLSqlfile = "/sqls/fndFormFunctionTL.sql";
	public static  String fndMenusTLSqlfile = "/sqls/fndMenusTL.sql";

	// Counters for unique IDs
	private static int instanceCounter = 10;
	private static int instanceTLCounter = 10;
	private static int functionCounter = 10;
	private static int functionTLCounter = 10;
	private static int menuCounter = 10;
	private static int menuTLCounter = 10;

	/**
	 * 
	 * @param ootbObjectMap
	 * @param targetOootbMap
	 * 
	 *            Will compare the target and the source seed data and create insert
	 *            queries to fix the target side issues.
	 */
	@SuppressWarnings("unchecked")
	public static void validate(Map<String, Map<DataSecurityObjects, List<Object>>> ootbObjectMap,
			Map<String, Map<DataSecurityObjects, List<Object>>> targetOootbMap) {

		long start = System.currentTimeMillis();

		// List of seed data to be inserted into target side

		List<FndGrant> fndGrants = null;
		List<FndObjectInstanceSet> instacneSets = null;
		List<FndMenu> fndMenus = null;
		List<FndFormFunction> fndFormFunctions = null;

		for (String objName : ootbObjectMap.keySet()) {

			// Removing the Source seed data already present in the target
			if (targetOootbMap.containsKey(objName)) {

				FndGrantTarget fndObjNameID = (FndGrantTarget) targetOootbMap.get(objName)
						.get(DataSecurityObjects.GRANTS).get(0);
				String objId = fndObjNameID.getFndObj().getObjectId().toString();

				if (ootbObjectMap.get(objName).get(DataSecurityObjects.GRANTS) != null
						&& targetOootbMap.get(objName).get(DataSecurityObjects.GRANTS) != null)

					fndGrants = (List<FndGrant>) (List) filterLists(
							ootbObjectMap.get(objName).get(DataSecurityObjects.GRANTS),
							targetOootbMap.get(objName).get(DataSecurityObjects.GRANTS), DataSecurityObjects.GRANTS);

				if (ootbObjectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS) != null
						&& targetOootbMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS) != null)
					instacneSets = (List<FndObjectInstanceSet>) (List) filterLists(
							ootbObjectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS),
							targetOootbMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS),
							DataSecurityObjects.INSTANCE_SETS);

				// if (ootbObjectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS) != null
				// && targetOootbMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS) != null
				// && DataSecurityProccessor.getMissingInstacneSetMap().containsKey(objName))
				// instacneSets = (List<FndObjectInstanceSet>) (List) filterLists(
				// DataSecurityProccessor.getMissingInstacneSetMap().get(objName),
				// targetOootbMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS),
				// DataSecurityObjects.INSTANCE_SETS);

				if (ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS) != null
						&& targetOootbMap.get(objName).get(DataSecurityObjects.MENUS) != null)
					fndMenus = (List<FndMenu>) (List) filterLists(
							ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS),
							targetOootbMap.get(objName).get(DataSecurityObjects.MENUS), DataSecurityObjects.MENUS);

				// if (ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS) != null
				// && targetOootbMap.get(objName).get(DataSecurityObjects.MENUS) != null
				// && DataSecurityProccessor.getMissingMenuMap().containsKey(objName))
				// fndMenus = (List<FndMenu>) (List) filterLists(
				// DataSecurityProccessor.getMissingMenuMap().get(objName),
				// targetOootbMap.get(objName).get(DataSecurityObjects.MENUS),
				// DataSecurityObjects.MENUS);

				if (ootbObjectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS) != null
						&& targetOootbMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS) != null)
					fndFormFunctions = (List<FndFormFunction>) (List) filterLists(
							ootbObjectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS),
							targetOootbMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS),
							DataSecurityObjects.FORM_FUNCIONS);

				// Considering only the seed data having created by = SEED_DATA_FROM_APPLICATION
				// Generating the sql queries corresponding to the data to be created in the
				// target DB

				/**
				 * 
				 * No need to create queries for grants if (fndGrants != null &&
				 * fndGrants.size() != 0) {
				 * 
				 * List<FndGrant> filteredFndGrants = new ArrayList<FndGrant>(); for (FndGrant
				 * fndGrant : fndGrants ) { if
				 * ("SEED_DATA_FROM_APPLICATION".equals(fndGrant.getCreatedBy()) ||
				 * "CUSTOMIZED_USER".equals(fndGrant.getCreatedBy())) {
				 * filteredFndGrants.add(fndGrant); } }
				 * 
				 * fndGrants = filteredFndGrants;
				 * 
				 * 
				 * // need to give a check if (fndGrants != null && fndGrants.size() != 0)
				 * generateSqlQueries(fndGrants, fndGrantSqlfile, objName, objId,
				 * targetOootbMap); }
				 **/

				if (instacneSets != null && instacneSets.size() != 0) {

					List<FndObjectInstanceSet> filteredInstacneSets = new ArrayList<FndObjectInstanceSet>();
					for (FndObjectInstanceSet fndObjectInstanceSet : instacneSets) {
						if ("SEED_DATA_FROM_APPLICATION".equals(fndObjectInstanceSet.getCreatedBy())
								|| "CUSTOMIZED_USER".equals(fndObjectInstanceSet.getCreatedBy())) {
							filteredInstacneSets.add(fndObjectInstanceSet);
						}
					}

					instacneSets = filteredInstacneSets;

					if (instacneSets != null && instacneSets.size() != 0)
						generateSqlQueries(instacneSets, fndInstanceSetsSqlfile, objName, objId, targetOootbMap);
					generateSqlQueries(instacneSets, fndInstanceSetsTLSqlfile, objName, objId, targetOootbMap);
				}

				if (fndMenus != null && fndMenus.size() != 0) {

					// Need to give a null check for createdBy
					List<FndMenu> filteredFndMenus = new ArrayList<FndMenu>();
					for (FndMenu fndMenu : fndMenus) {
						if ("SEED_DATA_FROM_APPLICATION".equals(fndMenu.getCreatedBy())
								|| "CUSTOMIZED_USER".equals(fndMenu.getCreatedBy())) {
							filteredFndMenus.add(fndMenu);
						}
					}
					fndMenus = filteredFndMenus;

					if (fndMenus != null && fndMenus.size() != 0)
						generateSqlQueries(fndMenus, fndMenusSqlfile, objName, objId, targetOootbMap);
					generateSqlQueries(fndMenus, fndMenusTLSqlfile, objName, objId, targetOootbMap);
				}

				if (fndFormFunctions != null && fndFormFunctions.size() != 0) {

					List<FndFormFunction> filteredFormFunctions = new ArrayList<FndFormFunction>();
					for (FndFormFunction fndFormFunction : fndFormFunctions) {
						if ("SEED_DATA_FROM_APPLICATION".equals(fndFormFunction.getCreatedBy())
								|| "CUSTOMIZED_USER".equals(fndFormFunction.getCreatedBy())) {
							filteredFormFunctions.add(fndFormFunction);
						}
					}
					fndFormFunctions = filteredFormFunctions;

					if (fndFormFunctions != null && fndFormFunctions.size() != 0)
						generateSqlQueries(fndFormFunctions, fndFormFunctionSqlfile, objName, objId, targetOootbMap);
					generateSqlQueries(fndFormFunctions, fndFormFunctionTLSqlfile, objName, objId, targetOootbMap);
				}
			}

		}

		logger.log(Level.INFO,
				"Total time taken for validation " + (System.currentTimeMillis() - start) + " milli seconds");

		logger.log(Level.INFO,
				"Check the sql files : \n" + fndInstanceSetsSqlfile + "\n" + fndMenusSqlfile + "\n"
						+ fndFormFunctionSqlfile + "\n"
						+ " and execute in the target DB to rectify the mismatch in seedatas in target side");
	}

	// Filter the source map with target map data , will return list having data in
	// source which are not in target

	public static List<Object> filterLists(List<Object> sourceOotb, List<Object> targetOotb,
			DataSecurityObjects datasecurityObject) {

		List<Object> pendingInSource = new ArrayList<Object>();

		// sourceDuplicate.removeAll(targetOotb);

		switch (datasecurityObject) {

		case GRANTS:
			List<FndGrant> sourceFndGrants = new ArrayList<>();
			for (Object obj : sourceOotb) {
				sourceFndGrants.add((FndGrant) obj);
			}
			List<FndGrantTarget> targetFndGrants = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndGrants.add((FndGrantTarget) obj);
			}

			for (FndGrant sourceGrant : sourceFndGrants) {
				boolean found = false;
				for (FndGrantTarget targetGrant : targetFndGrants) {
					if (sourceGrant.getGrantGuid().equals(targetGrant.getGrantGuid())) {
						found = true;
						break;
					}
				}
				if (!found) {
					pendingInSource.add(sourceGrant);
				}
			}
			break;

		case INSTANCE_SETS:
			List<FndObjectInstanceSet> sourceFndObjectInstacneSets = new ArrayList<>();
			for (Object obj : sourceOotb) {
				sourceFndObjectInstacneSets.add((FndObjectInstanceSet) obj);
			}
			List<FndObjectInstanceSetTarget> targetFndObjectInstacneSets = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndObjectInstacneSets.add((FndObjectInstanceSetTarget) obj);
			}

			for (FndObjectInstanceSet sourceInstanceSet : sourceFndObjectInstacneSets) {
				boolean found = false;
				for (FndObjectInstanceSetTarget targetInstacneSet : targetFndObjectInstacneSets) {
					if (targetInstacneSet != null)
						if (sourceInstanceSet.getInstanceSetName().equals(targetInstacneSet.getInstanceSetName())) {
							found = true;
							break;
						}
				}
				if (!found) {
					if (!"".equals(sourceInstanceSet.getInstanceSetName()))
						pendingInSource.add(sourceInstanceSet);
				}
			}
			break;

		case MENUS:
			List<FndMenu> sourceFndMenus = new ArrayList<>();
			for (Object obj : sourceOotb) {
				sourceFndMenus.add((FndMenu) obj);
			}
			List<FndMenuTarget> targetFndMenus = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndMenus.add((FndMenuTarget) obj);
			}

			for (FndMenu sourceMenu : sourceFndMenus) {
				boolean found = false;
				for (FndMenuTarget targetMenu : targetFndMenus) {
					if (sourceMenu.getMenuName().equals(targetMenu.getMenuName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					pendingInSource.add(sourceMenu);
				}
			}
			break;

		case FORM_FUNCIONS:
			List<FndFormFunction> sourceFndFormFunctions = new ArrayList<>();
			for (Object obj : sourceOotb) {
				sourceFndFormFunctions.add((FndFormFunction) obj);
			}
			List<FndFormFunctionTarget> targetFndFormFunctions = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndFormFunctions.add((FndFormFunctionTarget) obj);
			}

			for (FndFormFunction sourceFormFunction : sourceFndFormFunctions) {
				boolean found = false;
				for (FndFormFunctionTarget targetFormFunction : targetFndFormFunctions) {
					if (sourceFormFunction.getFunctionName().equals(targetFormFunction.getFunctionName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					pendingInSource.add(sourceFormFunction);
				}
			}
			break;

		default:
			logger.log(Level.INFO, "Invalid Data Securiy Object");
			break;
		}

		return pendingInSource;
	}

	// Add entries to corresponding sql files matching the data coming

	public static <T> void generateSqlQueries(List<T> list, String sqlFilename, String objName, String objId,
			Map<String, Map<DataSecurityObjects, List<Object>>> targetOootbMap) {

		try (FileWriter fw = new FileWriter(sqlFilename, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			Date currentDate = new Date();
			SimpleDateFormat sdf3 = new SimpleDateFormat("ddMMyyyy");
			String datePrefix = sdf3.format(currentDate) + "00";

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS aa");
			String formattedDate = dateFormat.format(currentDate);

			String toTabletimeStmap = "to_timestamp('" + formattedDate + "','" + "DD-MON-RR HH.MI.SSXFF AM')";
			String user = "APPLICATION_IMPLEMENTATION_CONSULTANT";

			if (list.get(0) instanceof FndGrant) {

				for (T fndGrant : list) {
					FndGrant grant = (FndGrant) fndGrant;
					out.println(String.format(
							"INSERT into FND_GRANTS (GRANT_GUID,NAME,CREATED_BY,OBJECT_ID,MENU_ID) VALUES (\"%s\",\"%s\",\"%s\",\"%s\");",
							grant.getGrantGuid(), grant.getName(), grant.getCreatedBy(), objId));
				}

			} else if (list.get(0) instanceof FndObjectInstanceSet) {
				for (T fndObjectInstanceSet : list) {

					FndObjectInstanceSet instanceSet = (FndObjectInstanceSet) fndObjectInstanceSet;

					if (sqlFilename.contains("TL")) {
						String instanceSetId = "22" + datePrefix + instanceTLCounter;

						out.println(String.format(
								"Insert into FND_OBJECT_INSTANCE_SETS_TL (INSTANCE_SET_ID,LANGUAGE,SOURCE_LANG,DISPLAY_NAME,DESCRIPTION,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,DISPLAY_SHORT_NAME,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values"
										+ "('%s','US','US','Access the sales forecast item for table"
										+ instanceSet.getInstanceSetName()
										+ "for the territory hierarchy that they owned previously for the active forecast for test 00"
										+ instanceTLCounter + "',null,'%s',%s,'%s',%s,'-1',1,null,'1','N',null);",
								instanceSetId, user, toTabletimeStmap, user, toTabletimeStmap));

						out.println(String.format(
								"Insert into FND_OBJECT_INSTANCE_SETS_TL (INSTANCE_SET_ID,LANGUAGE,SOURCE_LANG,DISPLAY_NAME,DESCRIPTION,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,DISPLAY_SHORT_NAME,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values"
										+ "('%s','KO','US','Access the sales forecast item for table"
										+ instanceSet.getInstanceSetName()
										+ "for the territory hierarchy that they owned previously for the active forecast for test 00"
										+ instanceTLCounter + "',null,'%s',%s,'%s',%s,'-1',1,null,'1','N',null);",
								instanceSetId, user, toTabletimeStmap, user, toTabletimeStmap));
						instanceTLCounter++;

					} else {
						String instanceSetId = "22" + datePrefix + instanceCounter;
						out.println(String.format(
								"Insert into FND_OBJECT_INSTANCE_SETS (INSTANCE_SET_ID,INSTANCE_SET_NAME,OBJECT_ID,PREDICATE,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,SANDBOX_ID,PROGRAM_NAME,PROGRAM_TAG,FILTER,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) VALUES "
										+ "('%s','%s','%s','1=1','%s',%s,'%s',%s,'-1',1,'1',null,null,null,'N',null);",
								instanceSetId, instanceSet.getInstanceSetName(), objId, user, toTabletimeStmap, user,
								toTabletimeStmap));
						instanceCounter++;
					}

				}

			} else if (list.get(0) instanceof FndFormFunction) {

				for (T fndFormFunction : list) {
					FndFormFunction formFunction = (FndFormFunction) fndFormFunction;
					FndFormFunctionTarget testFunction = (FndFormFunctionTarget) targetOootbMap
							.get(formFunction.getObjectName()).get(DataSecurityObjects.FORM_FUNCIONS).get(0);
					String moduleId = testFunction.getFndObject().getModuleId();

					if (sqlFilename.contains("TL")) {
						String functionId = "44" + datePrefix + functionTLCounter;
						out.println(String.format(
								"Insert into FND_FORM_FUNCTIONS_TL (FUNCTION_ID,LANGUAGE,SOURCE_LANG,USER_FUNCTION_NAME,DESCRIPTION,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values"
										+ " ('%s','US','US','%s','%s','%s',%s,'%s',%s,'-1',1,'1','N',null);",
								functionId, formFunction.getFunctionName(), formFunction.getFunctionName(), user,
								toTabletimeStmap, user, toTabletimeStmap));

						out.println(String.format(
								"Insert into FND_FORM_FUNCTIONS_TL (FUNCTION_ID,LANGUAGE,SOURCE_LANG,USER_FUNCTION_NAME,DESCRIPTION,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values"
										+ " ('%s','KO','US','%s','%s','%s',%s,'%s',%s,'-1',1,'1','N',null);",
								functionId, formFunction.getFunctionName(), formFunction.getFunctionName(), user,
								toTabletimeStmap, user, toTabletimeStmap));
						functionTLCounter++;

					} else {
						String functionId = "44" + datePrefix + functionCounter;
						out.println(String.format(
								"Insert into FND_FORM_FUNCTIONS (FUNCTION_ID,FUNCTION_NAME,OBJECT_ID,MODULE_ID,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,FUNCTION_SECURITY_ONLY,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values "
										+ " ('%s','%s','%s','%s','%s',%s,'%s',%s,'-1',1,'N','1','N',null);",
								functionId, formFunction.getFunctionName(), objId, moduleId, user, toTabletimeStmap,
								user, toTabletimeStmap));
						functionCounter++;
					}

				}

			} else if (list.get(0) instanceof FndMenu) {

				for (T fndMenu : list) {
					FndMenu menu = (FndMenu) fndMenu;
					FndObjectInstanceSetTarget fndObjectInstanceSetTarget = (FndObjectInstanceSetTarget) targetOootbMap
							.get(objName).get(DataSecurityObjects.INSTANCE_SETS).get(0);
					String moduleid = fndObjectInstanceSetTarget.getFndObj().getModuleId();

					if (sqlFilename.contains("TL")) {
						String menuId = "66" + datePrefix + menuTLCounter;
						out.println(String.format(
								"Insert into FND_MENUS_TL (MENU_ID,LANGUAGE,SOURCE_LANG,USER_MENU_NAME,DESCRIPTION,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values "
										+ "('%s','US','US','%s','%s','%s',%s,'%s',%s,'-1',1,'1','N',null);",
								menuId, menu.getMenuName(), menu.getMenuName() + " for test 00" + menuTLCounter, user,
								toTabletimeStmap, user, toTabletimeStmap));

						out.println(String.format(
								"Insert into FND_MENUS_TL (MENU_ID,LANGUAGE,SOURCE_LANG,USER_MENU_NAME,DESCRIPTION,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values "
										+ "('%s','KO','US','%s','%s','%s',%s,'%s',%s,'-1',1,'1','N',null);",
								menuId, menu.getMenuName(), menu.getMenuName() + " for test 00" + menuTLCounter, user,
								toTabletimeStmap, user, toTabletimeStmap));

						menuTLCounter++;
					} else {
						String menuId = "66" + datePrefix + menuCounter;
						out.println(String.format(
								"Insert into FND_MENUS (MENU_ID,MENU_NAME,MODULE_ID,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE,LAST_UPDATE_LOGIN,ENTERPRISE_ID,SANDBOX_ID,CHANGE_SINCE_LAST_REFRESH,SEED_DATA_SOURCE) values ('%s','%s','%s','%s',%s,'%s',%s,'-1',1,'1','N',null);",
								menuId, menu.getMenuName(), moduleid, user, toTabletimeStmap, user, toTabletimeStmap));

						menuCounter++;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();

		}

		return;
	}

	public static void truncateExistingFiles() {

		try {

			logger.log(Level.INFO, "Validating SQL files and truncating if already exists");
			File instanceSetFile = new File(fndInstanceSetsSqlfile);
			instanceSetFile.getParentFile().mkdirs();

			if (!instanceSetFile.createNewFile()) {
				Files.write(Paths.get(fndInstanceSetsSqlfile), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
			}

			File instanceSetTLfile = new File(fndInstanceSetsTLSqlfile);
			if (!instanceSetTLfile.createNewFile()) {
				Files.write(Paths.get(fndInstanceSetsTLSqlfile), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
			}

			File formFunctionFile = new File(fndFormFunctionSqlfile);
			if (!formFunctionFile.createNewFile()) {
				Files.write(Paths.get(fndFormFunctionSqlfile), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
			}

			File formFunctionTLfile = new File(fndFormFunctionTLSqlfile);
			if (!formFunctionTLfile.createNewFile()) {
				Files.write(Paths.get(fndFormFunctionTLSqlfile), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
			}

			File menuFile = new File(fndMenusSqlfile);
			if (!menuFile.createNewFile()) {
				Files.write(Paths.get(fndMenusSqlfile), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
			}

			File menuTLfile = new File(fndMenusTLSqlfile);
			if (!menuTLfile.createNewFile()) {
				Files.write(Paths.get(fndMenusTLSqlfile), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
