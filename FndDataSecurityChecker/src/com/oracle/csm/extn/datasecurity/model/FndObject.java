package com.oracle.csm.extn.datasecurity.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FndObjects")
public class FndObject {
	@XmlElement(name = "ObjName")
	private String objName;
	@XmlElement(name = "ModuleId")
	private String moduleId;
	@XmlElement(name = "CreatedBy")
	private String createdBy;
	@XmlElement(name = "LastUpdatedBy")
	private String lastUpdatedBy;

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

	@XmlElement(name = "FndObjectInstanceSets")
	private List<FndObjectInstanceSet> fndObjectInstanceSets;

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public List<FndObjectInstanceSet> getFndObjectInstanceSets() {
		return fndObjectInstanceSets;
	}

	public void setFndObjectInstanceSets(List<FndObjectInstanceSet> fndObjectInstanceSets) {
		this.fndObjectInstanceSets = fndObjectInstanceSets;
	}

	@Override
	public String toString() {
		return "FndObjectTarget [objName=" + objName + ", moduleId=" + moduleId + ", createdBy=" + createdBy
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", fndObjectInstanceSets=" + fndObjectInstanceSets + "]";
	}

}
