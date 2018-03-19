package com.oracle.csm.extn.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FND_OBJECT_INSTANCE_SETS")
public class FndObjectInstanceSetTarget {

	public FndObjectInstanceSetTarget() {
		// TODO Auto-generated constructor stub
	}

	@Id
    @Column(name="instance_set_id")
	private Long instanceSetId;
	@Column(name="instance_set_name")
	private String instanceSetName;
	@Column(name="predicate")
	private String predicate;
    @Column(name="created_by")
	private String createdBy;
    @Column(name="last_updated_by")
	private String lastUpdatedBy;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "object_id")
	private FndObjectTarget fndObj;

	public Long getInstanceSetId() {
		return instanceSetId;
	}
	public void setInstanceSetId(Long instanceSetId) {
		this.instanceSetId = instanceSetId;
	}
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
	public FndObjectTarget getFndObj() {
		return fndObj;
	}
	public void setFndObj(FndObjectTarget fndObj) {
		this.fndObj = fndObj;
	}
	

}
