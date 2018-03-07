package com.oracle.csm.extn.datasecurity;

import com.oracle.csm.extn.datasecurity.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.domain.FndObject;

public class DAOTest {
	public static void main(String[] args) {
		FndSecurityDAO dao = new FndSecurityDAO();
		FndObject fndObj = new FndObject();
		Long myInt = new Long("300100089560733");
		fndObj.setObjectId(myInt);
		fndObj.setObjName("FND_DIAG_TAG");
		fndObj.setModuleId("68AF5B001CDDDCF2E04044985FC6054E");
		fndObj.setLastUpdatedBy("SEED_DATA_FROM_APPLICATION");
		fndObj.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		// dao.getFndObject(fndObj);

		FndObject fndObj1 = new FndObject();

		Long myInt1 = new Long("300100089560735");
		fndObj1.setObjectId(myInt);
		fndObj1.setObjName("FND_DIAG_TEST");
		fndObj1.setModuleId("68AF5B001CDDDCF2E04044985FC6054E");
		fndObj1.setLastUpdatedBy("SEED_DATA_FROM_APPLICATION");
		fndObj1.setCreatedBy("SEED_DATA_FROM_APPLICATION");
		// dao.getFndObject(fndObj1);

		dao.getFndObject(fndObj);

	}
}
