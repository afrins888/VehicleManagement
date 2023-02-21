package com.cts.vehiclemanagement.Dto;

public class UserDTO {
	public long userid;
	public String username;
	public String firstname;
	public String lastname;
	public long dateofbirth;
	public boolean status;
	public  String Role;
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public long getDateofbirth() {
		return dateofbirth;
	}
	public void setDateofbirth(long dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		this.Role = role;
	}



	
}



