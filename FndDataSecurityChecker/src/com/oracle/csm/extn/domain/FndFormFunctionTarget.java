package com.oracle.csm.extn.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "FND_FORM_FUNCTIONS")
public class FndFormFunctionTarget {

	@Id
    @Column(name="function_id")
	private Long functionId;
	
    @Column(name="function_name")
	private String functionName;
    
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="object_id")
	private FndObjectTarget fndObject;
    
	@Column(name="module_id")
	private String moduleId;
    
	@Column(name="created_by")
	private String createdBy;
    
	@Column(name="last_updated_by")
	private String lastUpdatedBy;
   
	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public FndObjectTarget getFndObject() {
		return fndObject;
	}

	public void setFndObject(FndObjectTarget fndObject) {
		this.fndObject = fndObject;
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
