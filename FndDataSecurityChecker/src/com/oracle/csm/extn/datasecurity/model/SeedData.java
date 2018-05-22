package com.oracle.csm.extn.datasecurity.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;

@XmlRootElement(name = "SEEDDATA")
public class SeedData {

	@XmlElement(name = "FndGrants")
	private List<FndGrant> fndGrants;

	@XmlElement(name = "FndObjects")
	private List<FndObject> fndObjects;

	@XmlElement(name = "FndFormFunctions")
	private List<FndFormFunction> fndFormFunctions;

	@XmlElement(name = "FndMenus")
	private List<FndMenu> fndMenus;

	public List<FndMenu> getFndMenus() {
		return fndMenus;
	}

	public void setFndMenus(List<FndMenu> fndMenus) {
		this.fndMenus = fndMenus;
	}

	public List<FndGrant> getFndGrants() {
		return fndGrants;
	}

	public void setFndGrants(List<FndGrant> fndGrants) {
		this.fndGrants = fndGrants;
	}

	public List<FndObject> getFndObjects() {
		return fndObjects;
	}

	public void setFndObjects(List<FndObject> fndObjects) {
		this.fndObjects = fndObjects;
	}

	public List<FndFormFunction> getFndFormFunctions() {
		return fndFormFunctions;
	}

	public void setFndFormFunctions(List<FndFormFunction> fndFormFunctions) {
		this.fndFormFunctions = fndFormFunctions;
	}

	@Override
	public String toString() {
		return "SeedData [fndGrants=" + fndGrants + ", fndObjects=" + fndObjects + ", fndFormFunctions="
				+ fndFormFunctions + ", fndMenus=" + fndMenus + "]";
	}

}
