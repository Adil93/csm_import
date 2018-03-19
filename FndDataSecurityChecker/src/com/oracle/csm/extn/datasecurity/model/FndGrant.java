package com.oracle.csm.extn.datasecurity.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FndGrants")
public class FndGrant {

	@XmlElement(name = "GrantGuid")
	private String grantGuid;
	@XmlElement(name = "Name")
	private String name;
	@XmlElement(name = "GranteeType")
	private String granteeType;
	@XmlElement(name = "ModuleId")
	private String moduleId;
	@XmlElement(name = "FndObjects2.ObjName")
	private String objName;
	@XmlElement(name = "FndObjectInstanceSets1.InstanceSetName")
	private String instanceSetName;
	@XmlElement(name = "FndMenus1.MenuName")
	private String menuName;
	@XmlElement(name = "CreatedBy")
	private String createdBy;
	@XmlElement(name = "LastUpdatedBy")
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "FndGrantTarget [grantGuid=" + grantGuid + ", granteeType=" + granteeType + ", moduleId=" + moduleId
				+ ", objName=" + objName + ", instanceSetName=" + instanceSetName + ", menuName=" + menuName
				+ ", createdBy=" + createdBy + ", lastUpdatedBy=" + lastUpdatedBy + "]";
	}

}
