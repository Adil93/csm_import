package com.oracle.csm.extn.datasecurity.target;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.oracle.csm.extn.datasecurity.domain.FndFormFunction;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.model.FndMenu;
import com.oracle.csm.extn.datasecurity.model.FndObjectInstanceSet;

/**
 * 
 * @author adilmuthukoya
 *
 */
public class TargetValidator {

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

		// List of seed datas to be inserted into taget side

		List<FndGrant> fndGrants = null;
		List<FndObjectInstanceSet> instacneSets = null;
		List<FndMenu> fndMenus = null;
		List<FndFormFunction> fndFormFunctions = null;

		for (String objName : ootbObjectMap.keySet()) {

			//Removing the Source seed datas already present in the target
			
			if (ootbObjectMap.get(objName).get(DataSecurityObjects.GRANTS) != null
					&& targetOootbMap.get(objName).get(DataSecurityObjects.GRANTS) != null)
				fndGrants = (List<FndGrant>) (List) filterLists(
						ootbObjectMap.get(objName).get(DataSecurityObjects.GRANTS),
						targetOootbMap.get(objName).get(DataSecurityObjects.GRANTS));

			if (ootbObjectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS) != null
					&& targetOootbMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS) != null)
				instacneSets = (List<FndObjectInstanceSet>) (List) filterLists(
						ootbObjectMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS),
						targetOootbMap.get(objName).get(DataSecurityObjects.INSTANCE_SETS));

			if (ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS) != null
					&& targetOootbMap.get(objName).get(DataSecurityObjects.MENUS) != null)
				fndMenus = (List<FndMenu>) (List) filterLists(ootbObjectMap.get(objName).get(DataSecurityObjects.MENUS),
						targetOootbMap.get(objName).get(DataSecurityObjects.MENUS));

			if (ootbObjectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS) != null
					&& targetOootbMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS) != null)
				fndFormFunctions = (List<FndFormFunction>) (List) filterLists(
						ootbObjectMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS),
						targetOootbMap.get(objName).get(DataSecurityObjects.FORM_FUNCIONS));

			//Considering only the seed data having created by = SEED_DATA_FROM_APPLICATION
			// Generating the sql queries corresponding to the data to be create in the target DB 
			
			if (fndGrants != null && fndGrants.size() != 0) {
				fndGrants = fndGrants.stream()
						.filter(fndGrant -> fndGrant.getCreatedBy().equals("SEED_DATA_FROM_APPLICATION"))
						.collect(Collectors.toList());
				generateSqlQueries(fndGrants, fndGrantSqlfile);
			}

			if (instacneSets != null && instacneSets.size() != 0) {

				instacneSets = instacneSets.stream()
						.filter(instanceSet -> instanceSet.getCreatedBy().equals("SEED_DATA_FROM_APPLICATION"))
						.collect(Collectors.toList());
				generateSqlQueries(instacneSets, fndInstanceSetsSqlfile);
			}

			if (fndMenus != null && fndMenus.size() != 0) {
				fndMenus = fndMenus.stream()
						.filter(fndMenu -> fndMenu.getCreatedBy().equals("SEED_DATA_FROM_APPLICATION"))
						.collect(Collectors.toList());
				generateSqlQueries(fndMenus, fndMenusSqlfile);
			}

			if (fndFormFunctions != null && fndFormFunctions.size() != 0) {
				fndFormFunctions = fndFormFunctions.stream()
						.filter(fndFormFunction -> fndFormFunction.getCreatedBy().equals("SEED_DATA_FROM_APPLICATION"))
						.collect(Collectors.toList());
				generateSqlQueries(fndFormFunctions, fndFormFunctionSqlfile);
			}

		}

	}

	public static <T> void generateSqlQueries(List<T> list, String sqlFilename) {

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
							formFunction.getFunctionName(), formFunction.getCreatedBy()));
				}

			} else if (list.get(0) instanceof FndMenu) {
				for (T fndMenu : list) {
					FndMenu menu = (FndMenu) fndMenu;

					out.println(String.format(
							"INSERT into FND_MENUS (MENU_NAME,CREATED_BY) VALUES (\"%s\",\"%s\");",
							menu.getMenuName(), menu.getCreatedBy()));
				}

			}
		} catch (IOException e) {
			e.printStackTrace();

		}

		return;
	}

	private static <T> List<T> filterLists(List<T> sourceOotb, List<T> targetOotb) {

		// just not to alter the original list
		List<T> sourceDuplicate = new ArrayList<T>(sourceOotb);
		sourceDuplicate.removeAll(targetOotb);

		return sourceDuplicate;
	}
}
