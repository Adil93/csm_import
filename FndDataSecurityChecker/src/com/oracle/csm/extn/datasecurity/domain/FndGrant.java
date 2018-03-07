package com.oracle.csm.extn.datasecurity.domain;


public class FndGrant {

	private String grantGuid;
	private String granteeType;
	private String moduleId;
	private String objName;
	private String instanceSetName;
	private String menuName;
	private String createdBy;
	private String lastUpdatedBy;

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getInstanceSetName() {
		return instanceSetName;
	}

	public void setInstanceSetName(String instanceSetName) {
		this.instanceSetName = instanceSetName;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getGrantGuid() {
		return grantGuid;
	}

	public void setGrantGuid(String grantGuid) {
		this.grantGuid = grantGuid;
	}

	public String getGranteeType() {
		return granteeType;
	}

	public void setGranteeType(String granteeType) {
		this.granteeType = granteeType;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}



}
