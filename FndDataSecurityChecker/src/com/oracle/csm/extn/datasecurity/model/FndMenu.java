package com.oracle.csm.extn.datasecurity.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FndMenus")
public class FndMenu {

	public FndMenu() {
		// TODO Auto-generated constructor stub
	}
	
	public FndMenu(String menuName) {
		// TODO Auto-generated constructor stub
		this.menuName=menuName;
	}
	@XmlElement(name = "MenuName")
	private String menuName;

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

	@XmlElement(name = "FndMenuEntries")
	List<FndMenuEntries> fndMenuEntries;

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public List<FndMenuEntries> getFndMenuEntries() {
		return fndMenuEntries;
	}

	public void setFndMenuEntries(List<FndMenuEntries> fndMenuEntries) {
		this.fndMenuEntries = fndMenuEntries;
	}

	@Override
	public String toString() {
		return "FndMenu [menuName=" + menuName + ", createdBy=" + createdBy + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", fndMenuEntries=" + fndMenuEntries + "]";
	}

}
