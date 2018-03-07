package com.oracle.csm.extn.datasecurity.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "FND_MENUS")
public class FndMenu {

	public FndMenu() {
		// TODO Auto-generated constructor stub
	}
	
	@Column(name = "MENU_NAME")
	private String menuName;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATED_BY")
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

	
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	
}
