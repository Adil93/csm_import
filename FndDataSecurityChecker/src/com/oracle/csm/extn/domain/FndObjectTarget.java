package com.oracle.csm.extn.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "FND_OBJECTS")
public class FndObjectTarget implements Serializable{
	public FndObjectTarget(){
		
	}
	
	public FndObjectTarget(Long objectId, String objName, String moduleId, String createdBy, String lastUpdatedBy) {
		super();
		this.objectId = objectId;
		this.objName = objName;
		this.moduleId = moduleId;
		this.createdBy = createdBy;
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Id
    @Column(name="object_id")
	private Long objectId;
	@Column(name = "obj_name")
	private String objName;
	@Column(name = "module_id")
	private String moduleId;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
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