package com.oracle.csm.extn.datasecurity.source;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.oracle.csm.extn.datasecurity.dao.FndSecurityDAO;
import com.oracle.csm.extn.datasecurity.domain.FndGrantTarget;

public class CsmTargetCall implements Callable<List<FndGrantTarget>>{
	FndSecurityDAO dao = new FndSecurityDAO();
    private List<Long> fndObjIdList;
	
	public void setFndObjIdList(List<Long> fndObjIdList) {
		this.fndObjIdList = fndObjIdList;
	}

	@Override
	public List<FndGrantTarget> call() throws Exception {
		// TODO Auto-generated method stub
		
		List<FndGrantTarget> fndGrants=null;
		return fndGrants;
	}
	
	 List<Future<FndGrantTarget>> list = new ArrayList<Future<FndGrantTarget>>();
	 Callable<List<FndGrantTarget>> callable = new CsmTargetCall();
}
