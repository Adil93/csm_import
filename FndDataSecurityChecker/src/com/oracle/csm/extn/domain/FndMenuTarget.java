package com.oracle.csm.extn.domain;


import java.io.Serializable;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


/**
 * @author pminchin
 *
 */
@Entity
@Table(name = "FND_MENUS")
public class FndMenuTarget implements Serializable{

	public FndMenuTarget() {
		// TODO Auto-generated constructor stub
	}
	
	public FndMenuTarget(Long menuId, String menuName, String createdBy, String lastUpdatedBy) {
		super();
		this.menuId = menuId;
		this.menuName = menuName;
		this.createdBy = createdBy;
		this.lastUpdatedBy = lastUpdatedBy;
	}


	@Id
    @Column(name="menu_id")
	private Long menuId;
	@Column(name = "menu_name")
	private String menuName;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "fnd_menu_entries", joinColumns = {
			@JoinColumn(name = "menu_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "function_id",
					nullable = false, updatable = false) })
	private List<FndFormFunctionTarget> fndFormFunction;
	
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
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

	public List<FndFormFunctionTarget> getFndFormFunction() {
		return fndFormFunction;
	}

	public void setFndFormFunction(List<FndFormFunctionTarget> fndFormFunction) {
		this.fndFormFunction = fndFormFunction;
	}
	
	
}
