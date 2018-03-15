package com.oracle.csm.extn.datasecurity.target;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.oracle.csm.extn.datasecurity.model.FndFormFunction;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.model.FndMenu;
import com.oracle.csm.extn.datasecurity.model.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.utils.DSLoggerUtil;
import com.sun.media.jfxmedia.logging.Logger;

/**
 * 
 * @author adilmuthukoya
 *
 */
public class TargetValidator {
	private static java.util.logging.Logger logger = DSLoggerUtil.getLogger();

	private static final String fndGrantSqlfile = "sqls/fndGrants.sql";
	private static final String fndInstanceSetsSqlfile = "sqls/fndInstanceSets.sql";
	private static final String fndFormFunctionSqlfile = "sqls/fndFormFunctions.sql";
	private static final String fndMenusSqlfile = "sqls/fndMenus.sql";

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

		// Need to change the target models to target domain.<>
		
		


		try {
			Files.newBufferedWriter(Paths.get(fndGrantSqlfile), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get(fndInstanceSetsSqlfile), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get(fndFormFunctionSqlfile), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get(fndMenusSqlfile), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List of seed data to be inserted into target side

		List<FndGrant> fndGrants = null;
		List<FndObjectInstanceSet> instacneSets = null;
		List<FndMenu> fndMenus = null;
		List<FndFormFunction> fndFormFunctions = null;

		List<FndGrant> targetGndGrants = null;
		List<FndObjectInstanceSet> targetInstacneSets = null;
		List<FndMenu> targetFndMenus = null;
		List<FndFormFunction> targetFndFormFunctions = null;
		
		
		
		
		for (String objName : ootbObjectMap.keySet()) {

			// Removing the Source seed data already present in the target
			if (targetOootbMap.containsKey(objName)) {
				
				com.oracle.csm.extn.datasecurity.domain.FndGrant fndObjNameID= (com.oracle.csm.extn.datasecurity.domain.FndGrant) targetOootbMap.get(objName).get(DataSecurityObjects.GRANTS).get(0);
				String  objId= fndObjNameID.getFndObj().getObjectId().toString();

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

				if (ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS) != null
						&& targetOootbMap.get(objName).get(DataSecurityObjects.MENUS) != null)
					fndMenus = (List<FndMenu>) (List) filterLists(
							ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS),
							targetOootbMap.get(objName).get(DataSecurityObjects.MENUS), DataSecurityObjects.MENUS);

				if (ootbObjectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS) != null
						&& targetOootbMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS) != null)
					fndFormFunctions = (List<FndFormFunction>) (List) filterLists(
							ootbObjectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS),
							targetOootbMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS),
							DataSecurityObjects.FORM_FUNCIONS);

				// Considering only the seed data having created by = SEED_DATA_FROM_APPLICATION
				// Generating the sql queries corresponding to the data to be created in the
				// target DB

				if (fndGrants != null && fndGrants.size() != 0) {
					fndGrants = fndGrants.stream()
							.filter(fndGrant -> "SEED_DATA_FROM_APPLICATION".equals(fndGrant.getCreatedBy()))
							.collect(Collectors.toList());
					// need to give a check
					if (fndGrants != null && fndGrants.size() != 0)
						generateSqlQueries(fndGrants, fndGrantSqlfile,objId);
				}

				if (instacneSets != null && instacneSets.size() != 0) {

					instacneSets = instacneSets.stream()
							.filter(instanceSet -> "SEED_DATA_FROM_APPLICATION".equals(instanceSet.getCreatedBy()))
							.collect(Collectors.toList());
					if (instacneSets != null && instacneSets.size() != 0)
						generateSqlQueries(instacneSets, fndInstanceSetsSqlfile,objId);
				}

				 if (fndMenus != null && fndMenus.size() != 0) {
				
				 // Need to give a null check for createdBy
				 fndMenus = fndMenus.stream()
				 .filter(fndMenu ->
				 			
						 "SEED_DATA_FROM_APPLICATION".equals(fndMenu.getCreatedBy()))
				 .collect(Collectors.toList());
				 if (fndMenus != null && fndMenus.size() != 0)
				 generateSqlQueries(fndMenus, fndMenusSqlfile,objId);
				 }

				if (fndFormFunctions != null && fndFormFunctions.size() != 0) {
					fndFormFunctions = fndFormFunctions.stream().filter(
							fndFormFunction -> "SEED_DATA_FROM_APPLICATION".equals(fndFormFunction.getCreatedBy()))
							.collect(Collectors.toList());
					if (fndFormFunctions != null && fndFormFunctions.size() != 0)
						generateSqlQueries(fndFormFunctions, fndFormFunctionSqlfile,objId);
				}
			}

		}

	}

	// Add entries to corresponding sql files matching the data coming

	public static <T> void generateSqlQueries(List<T> list, String sqlFilename, String objId) {

		try (FileWriter fw = new FileWriter(sqlFilename, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			if (list.get(0) instanceof FndGrant) {

				for (T fndGrant : list) {
					FndGrant grant = (FndGrant) fndGrant;
					out.println(String.format(
							"INSERT into FND_GRANTS (NAME,CREATED_BY,OBJECT_ID,MENU_ID) VALUES (\"%s\",\"%s\");",
							grant.getName(), grant.getCreatedBy()));
				}

			} else if (list.get(0) instanceof FndObjectInstanceSet) {
				for (T fndObjectInstanceSet : list) {

					FndObjectInstanceSet instanceSet = (FndObjectInstanceSet) fndObjectInstanceSet;
					out.println(String.format(
							"INSERT into FND_OBJECT_INSTANCE_SET (INSTANCE_SET_NAME,CREATED_BY,OBJECT_ID,MENU_ID) VALUES (\"%s\",\"%s\");",
							instanceSet.getInstanceSetName(), instanceSet.getCreatedBy()));

				}

			} else if (list.get(0) instanceof FndFormFunction) {

				for (T fndFormFunction : list) {
					FndFormFunction formFunction = (FndFormFunction) fndFormFunction;

					out.println(String.format(
							"INSERT into FND_FORM_FUNCTIONS (FUNCTION_NAME,CREATED_BY,OBJECT_ID) VALUES (\"%s\",\"%s\");",
							formFunction.getFunctionName(), formFunction.getCreatedBy(),objId));
				}

			} else if (list.get(0) instanceof FndMenu) {
				for (T fndMenu : list) {
					FndMenu menu = (FndMenu) fndMenu;

					out.println(String.format("INSERT into FND_MENUS (MENU_NAME,CREATED_BY) VALUES (\"%s\",\"%s\");",
							menu.getMenuName(), menu.getCreatedBy()));
				}

			}
		} catch (IOException e) {
			e.printStackTrace();

		}

		return;
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
			List<com.oracle.csm.extn.datasecurity.domain.FndGrant> targetFndGrants = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndGrants.add((com.oracle.csm.extn.datasecurity.domain.FndGrant) obj);
			}

			for (FndGrant sourceGrant : sourceFndGrants) {
				boolean found = false;
				for (com.oracle.csm.extn.datasecurity.domain.FndGrant targetGrant : targetFndGrants) {
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
			List<com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSet> targetFndObjectInstacneSets = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndObjectInstacneSets.add((com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSet) obj);
			}

			for (FndObjectInstanceSet sourceInstanceSet : sourceFndObjectInstacneSets) {
				boolean found = false;
				for (com.oracle.csm.extn.datasecurity.domain.FndObjectInstanceSet targetInstacneSet : targetFndObjectInstacneSets) {
					if(targetInstacneSet !=null)
					if (sourceInstanceSet.getInstanceSetName().equals(targetInstacneSet.getInstanceSetName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					pendingInSource.add(sourceInstanceSet);
				}
			}
			break;
		case MENUS:
			List<FndMenu> sourceFndMenus = new ArrayList<>();
			for (Object obj : sourceOotb) {
				sourceFndMenus.add((FndMenu) obj);
			}
			List<com.oracle.csm.extn.datasecurity.domain.FndMenu> targetFndMenus = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndMenus.add((com.oracle.csm.extn.datasecurity.domain.FndMenu) obj);
			}

			for (FndMenu sourceMenu : sourceFndMenus) {
				boolean found = false;
				for (com.oracle.csm.extn.datasecurity.domain.FndMenu targetMenu : targetFndMenus) {
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
			List<com.oracle.csm.extn.datasecurity.model.FndFormFunction> sourceFndFormFunctions = new ArrayList<>();
			for (Object obj : sourceOotb) {
				sourceFndFormFunctions.add((FndFormFunction) obj);
			}
			List<com.oracle.csm.extn.datasecurity.domain.FndFormFunction> targetFndFormFunctions = new ArrayList<>();
			for (Object obj : targetOotb) {
				targetFndFormFunctions.add((com.oracle.csm.extn.datasecurity.domain.FndFormFunction) obj);
			}

			for (FndFormFunction sourceFormFunction : sourceFndFormFunctions) {
				boolean found = false;
				for (com.oracle.csm.extn.datasecurity.domain.FndFormFunction targetFormFunction : targetFndFormFunctions) {
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

}
