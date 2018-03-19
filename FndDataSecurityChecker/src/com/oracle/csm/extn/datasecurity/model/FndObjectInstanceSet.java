package com.oracle.csm.extn.datasecurity.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FndObjectInstanceSets")
public class FndObjectInstanceSet {

	public FndObjectInstanceSet() {
		// TODO Auto-generated constructor stub
	}
	public FndObjectInstanceSet(String instanceSetname) {
		this.instanceSetName=instanceSetname;
	}
	@XmlElement(name = "InstanceSetName")
	private String instanceSetName;
	@XmlElement(name = "Predicate")
	private String predicate;

	@XmlElement(name = "CreatedBy")
	private String createdBy;
	@XmlElement(name = "LastUpdatedBy")
	private String lastUpdatedBy;

	public String getInstanceSetName() {
		return instanceSetName;
	}

	public void setInstanceSetName(String instanceSetName) {
		this.instanceSetName = instanceSetName;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
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
		return "FndObjectInstanceSetTarget [instanceSetName=" + instanceSetName + ", predicate=" + predicate + ", createdBy="
				+ createdBy + ", lastUpdatedBy=" + lastUpdatedBy + "]";
	}

}
