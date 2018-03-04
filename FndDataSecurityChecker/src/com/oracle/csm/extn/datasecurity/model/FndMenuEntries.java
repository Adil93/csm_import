package com.oracle.csm.extn.datasecurity.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FndMenuEntries")
public class FndMenuEntries {

	@XmlElement(name = "SubMenuId")
	private String subMenuId;
	@XmlElement(name = "FndFormFunctions1.FunctionName")
	private String formFunction;
	@XmlElement(name = "FndFormFunctions1.FndObjects3.ObjName")
	private String objName;
	@XmlElement(name = "CreatedBy")
	private String createdBy;
	@XmlElement(name = "LastUpdatedBy")
	private String lastUpdatedBy;

	public String getSubMenuId() {
		return subMenuId;
	}

	public void setSubMenuId(String subMenuId) {
		this.subMenuId = subMenuId;
	}

	public String getFormFunction() {
		return formFunction;
	}

	public void setFormFunction(String formFunction) {
		this.formFunction = formFunction;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
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
		return "FndMenuEntries [subMenuId=" + subMenuId + ", formFunction=" + formFunction + ", objName=" + objName
				+ ", createdBy=" + createdBy + ", lastUpdatedBy=" + lastUpdatedBy + "]";
	}

}
