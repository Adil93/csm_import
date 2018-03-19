package com.oracle.csm.extn.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FND_GRANTS")
public class FndGrantTarget {
    

	@Id
	@Column(name = "grant_guid")
	private String grantGuid;

	@Column(name = "grantee_type")
	private String granteeType;

	@Column(name = "module_id")
	private String moduleId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "object_id")
	private FndObjectTarget fndObj;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "menu_id")
	private FndMenuTarget fndMenu;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

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

	public FndObjectTarget getFndObj() {
		return fndObj;
	}

	public void setFndObj(FndObjectTarget fndObj) {
		this.fndObj = fndObj;
	}

	public FndMenuTarget getFndMenu() {
		return fndMenu;
	}

	public void setFndMenu(FndMenuTarget fndMenu) {
		this.fndMenu = fndMenu;
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
