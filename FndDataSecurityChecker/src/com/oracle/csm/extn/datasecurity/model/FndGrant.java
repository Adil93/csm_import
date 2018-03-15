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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grantGuid == null) ? 0 : grantGuid.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((objName == null) ? 0 : objName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FndGrant other = (FndGrant) obj;
		
		if (grantGuid == null) {
			if (other.grantGuid != null)
				return false;
		} else if (!grantGuid.equals(other.grantGuid))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objName == null) {
			if (other.objName != null)
				return false;
		} else if (!objName.equals(other.objName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FndGrant [grantGuid=" + grantGuid + ", granteeType=" + granteeType + ", moduleId=" + moduleId
				+ ", objName=" + objName + ", instanceSetName=" + instanceSetName + ", menuName=" + menuName
				+ ", createdBy=" + createdBy + ", lastUpdatedBy=" + lastUpdatedBy + "]";
	}

}
