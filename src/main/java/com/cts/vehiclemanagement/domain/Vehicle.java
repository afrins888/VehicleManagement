package com.cts.vehiclemanagement.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="vehicle_table")
public class Vehicle {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="vehicle_id")
	private Long vehicleid;
	@Column(name="company_id")
	private Long companyid;
	@Column (name="vehicle_name")
	private String vehiclename;
	@Column (name="engine_cc")
	private Long enginecc;
	@Column (name="created_by")
	private Long createdBy;
	
	@Column (name="updated_by")
	private Long updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name="created_date")
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name="updated_on")
	private Date updatedOn;

	public Long getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(Long vehicleid) {
		this.vehicleid = vehicleid;
	}

	public Long getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	public String getVehiclename() {
		return vehiclename;
	}

	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}

	public Long getEnginecc() {
		return enginecc;
	}

	public void setEnginecc(Long enginecc) {
		this.enginecc = enginecc;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	

}
