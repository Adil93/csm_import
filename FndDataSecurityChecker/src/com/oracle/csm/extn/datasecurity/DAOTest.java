package com.oracle.csm.extn.datasecurity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.csm.extn.datasecurity.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.domain.FndObject;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.datasecurity.model.FndGrant;
import com.oracle.csm.extn.datasecurity.model.FndObjectInstanceSet;
import com.oracle.csm.extn.datasecurity.target.TargetValidator;

public class DAOTest {

	public static void main(String[] args) {
//		FndSecurityDAO dao = new FndSecurityDAO();
//		FndObject fndObj = new FndObject();
//		Long myInt = new Long("300100089560733");
//		fndObj.setObjectId(myInt);
//		fndObj.setObjName("FND_DIAG_TAG");
//		fndObj.setModuleId("68AF5B001CDDDCF2E04044985FC6054E");
//		fndObj.setLastUpdatedBy("SEED_DATA_FROM_APPLICATION");
//		fndObj.setCreatedBy("SEED_DATA_FROM_APPLICATION");
//		// dao.getFndObject(fndObj);
//
//		FndObject fndObj1 = new FndObject();
//
//		Long myInt1 = new Long("300100089560735");
//		fndObj1.setObjectId(myInt);
//		fndObj1.setObjName("FND_DIAG_TEST");
//		fndObj1.setModuleId("68AF5B001CDDDCF2E04044985FC6054E");
//		fndObj1.setLastUpdatedBy("SEED_DATA_FROM_APPLICATION");
//		fndObj1.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		// dao.getFndObject(fndObj1);

		// Maps

		Map<DataSecurityObjects, List<Object>> sourceObjectMap = new HashMap<DataSecurityObjects, List<Object>>();
		Map<DataSecurityObjects, List<Object>> targetObjectMap = new HashMap<DataSecurityObjects, List<Object>>();

		Map<String, Map<DataSecurityObjects, List<Object>>> sourceMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();
		Map<String, Map<DataSecurityObjects, List<Object>>> targetMap = new HashMap<String, Map<DataSecurityObjects, List<Object>>>();

		try {
			Files.newBufferedWriter(Paths.get("sqls/fndGrants.sql"), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get("sqls/fndInstanceSets.sql"), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get("sqls/fndFormFunctions.sql"), StandardOpenOption.TRUNCATE_EXISTING);
			Files.newBufferedWriter(Paths.get("sqls/fndMenus.sql"), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// dao.getFndObject(fndObj);

		FndGrant fndGrant = new FndGrant();
		fndGrant.setName("Grant001");
		fndGrant.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		fndGrant.setGrantGuid("123");

		FndGrant fndGrant1 = new FndGrant();
		fndGrant1.setName("Grant002");
		fndGrant1.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		fndGrant1.setGrantGuid("222");

		FndGrant fndGrant2 = new FndGrant();
		fndGrant2.setName("Grant003");
		fndGrant2.setCreatedBy("SEED_DATA_FROM_APPLICATION");

		FndGrant fndGrant3 = new FndGrant();
		fndGrant3.setName("Grant004");
		fndGrant3.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		
		
		
		

		List<FndGrant> fndGrants = new ArrayList<FndGrant>();
		fndGrants.add(fndGrant);
		fndGrants.add(fndGrant1);

		
		List<FndGrant> fndGrants1 = new ArrayList<FndGrant>();
		fndGrants1.add(fndGrant2);
		fndGrants1.add(fndGrant3);
		
		//FND instacne sets
		
		FndObjectInstanceSet fndObjectInstanceSet = new FndObjectInstanceSet("instance001");
		fndObjectInstanceSet.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		
		FndObjectInstanceSet fndObjectInstanceSet1 = new FndObjectInstanceSet("instance002");
		fndObjectInstanceSet1.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		
		FndObjectInstanceSet fndObjectInstanceSet2 = new FndObjectInstanceSet("instance003");
		fndObjectInstanceSet2.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		
		FndObjectInstanceSet fndObjectInstanceSet3 = new FndObjectInstanceSet("instance004");
		fndObjectInstanceSet3.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		
		List<FndObjectInstanceSet> instacneSets1 = new ArrayList<FndObjectInstanceSet>();
		instacneSets1.add(fndObjectInstanceSet);
		instacneSets1.add(fndObjectInstanceSet1);
		
		
		List<FndObjectInstanceSet> instacneSets2 = new ArrayList<FndObjectInstanceSet>();
		instacneSets2.add(fndObjectInstanceSet2);
		instacneSets2.add(fndObjectInstanceSet3);
		
		
		
		sourceObjectMap.put(DataSecurityObjects.GRANTS, (List) fndGrants);
		sourceObjectMap.put(DataSecurityObjects.INSTANCE_SETS, (List) instacneSets1);
		sourceObjectMap.put(DataSecurityObjects.FORM_FUNCIONS, null);
		sourceObjectMap.put(DataSecurityObjects.MENUS, null);
		
		targetObjectMap.put(DataSecurityObjects.GRANTS, (List)fndGrants1);
		targetObjectMap.put(DataSecurityObjects.INSTANCE_SETS, (List) instacneSets2);
		targetObjectMap.put(DataSecurityObjects.FORM_FUNCIONS, null);
		targetObjectMap.put(DataSecurityObjects.MENUS, null);

		sourceMap.put("abc", sourceObjectMap);

//		TargetValidator.generateSqlQueries(fndGrants, "sqls/fndGrants.sql");

	

//		TargetValidator.generateSqlQueries(fndGrants1, "sqls/fndGrants.sql");

		sourceObjectMap.put(DataSecurityObjects.GRANTS, (List) fndGrants);
		sourceMap.put("abc", sourceObjectMap);
		targetMap.put("abc",targetObjectMap );
		
		
//		TargetValidator.validate(sourceMap, targetMap);
		
		
		List<com.oracle.csm.extn.datasecurity.domain.FndGrant> targetfndGrants = new ArrayList<>();
		com.oracle.csm.extn.datasecurity.domain.FndGrant targetFndGrant = new com.oracle.csm.extn.datasecurity.domain.FndGrant();
		targetFndGrant.setGrantGuid("123");
		targetFndGrant.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		com.oracle.csm.extn.datasecurity.domain.FndGrant targetFndGrant1 = new com.oracle.csm.extn.datasecurity.domain.FndGrant();
		targetFndGrant1.setGrantGuid("222");
		targetFndGrant1.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		
		
		targetfndGrants.add(targetFndGrant);
		targetfndGrants.add(targetFndGrant1);
		
		List<Object> srcList=new ArrayList<>();
		List<Object> trgList=new ArrayList<>();
		
		for(FndGrant fndGrant4: fndGrants)
			srcList.add(fndGrant4);
		
		for(com.oracle.csm.extn.datasecurity.domain.FndGrant trgGrant :targetfndGrants ) {
			trgList.add(trgGrant);
		}
		
		List<FndGrant> result = (List<FndGrant>)(List)TargetValidator.filterLists(srcList, trgList, DataSecurityObjects.GRANTS);
		
		System.out.println("complete tesing");

	}
}
