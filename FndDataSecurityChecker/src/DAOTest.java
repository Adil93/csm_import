import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.csm.extn.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.model.DataSecurityObjects;
import com.oracle.csm.extn.domain.FndFormFunctionTarget;
import com.oracle.csm.extn.domain.FndGrantTarget;
import com.oracle.csm.extn.domain.FndMenuTarget;
import com.oracle.csm.extn.domain.FndObjectTarget;
import com.oracle.csm.extn.domain.FndObjectInstanceSetTarget;

public class DAOTest {
public static void main(String[] args) {
	FndSecurityDAO dao = new FndSecurityDAO();
	FndObjectTarget fndObj = new FndObjectTarget();
	Long myInt = new Long("300100110421917");
	fndObj.setObjectId(myInt);
	fndObj.setObjName("FND_DIAG_TAG");
	fndObj.setModuleId("68AF5B001CDDDCF2E04044985FC6054E");
	fndObj.setLastUpdatedBy("SEED_DATA_FROM_APPLICATION");
	fndObj.setCreatedBy("SEED_DATA_FROM_APPLICATION");

	
	FndObjectTarget fndObj1 = new FndObjectTarget();

	Long myInt1 = new Long("300100110421917");
	fndObj1.setObjectId(myInt);
	fndObj1.setObjName("FND_DIAG_TEST");
	fndObj1.setModuleId("68AF5B001CDDDCF2E04044985FC6054E");
	fndObj1.setLastUpdatedBy("SEED_DATA_FROM_APPLICATION");
	fndObj1.setCreatedBy("SEED_DATA_FROM_APPLICATION");

	
	//Query DB and get the Fnd Objects for names
	List<String> objName = new <String>ArrayList();
	objName.add("FND_DIAG_TAG");
	objName.add("FND_DIAG_TEST");
	
	Map<String, Map<DataSecurityObjects, List<Object>>> targetOotbObjectMap = new HashMap();
	
	Map<DataSecurityObjects, List<Object>> targetDs = new HashMap();

	List<FndObjectTarget> FndObjList = dao.getFndObject(objName);
	
	for (FndObjectTarget fndObject : FndObjList) {
	
		List grantList = new ArrayList();
		List menuList = new ArrayList();
		List fromFunctionList = new ArrayList();
		List fndInstanceSetList = new ArrayList();
		
		List<Long> objIdList = new <Long>ArrayList();
		
		objIdList.add(fndObject.getObjectId());
		//List<FndGrantTarget> result = dao.geFndGrants(objIdList);
	/*	for (FndGrantTarget fndGrant : result) {

			grantList.add(fndGrant);
			targetDs.put(DataSecurityObjects.GRANTS, grantList);
			
			menuList.add(fndGrant.getFndMenu());
			targetDs.put(DataSecurityObjects.MENUS, menuList);
			//fromFunctionList.add(fndGrant.get)

			fndInstanceSetList.add(fndGrant.getInstanceSet());	
			targetDs.put(DataSecurityObjects.INSTANCE_SETS, fndInstanceSetList);
			
		}
		
		targetOotbObjectMap.put(fndObject.getObjName(), targetDs);*/
        
	}
	

	
	
	

	
	
}
}
