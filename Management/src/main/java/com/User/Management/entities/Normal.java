package com.User.Management.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="NORMAL")
public class Normal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String f_name;
	private String l_name;
	@NotBlank(message = "Email is Mandatory")
	private String email;
	@NotBlank(message = "Phone Number is Mandatory")
	@Size(min = 10, max = 10,message = "Phone number should be 10 digit")
	private String phone;
	private String country;
	@Column(length = 5000)
	@NotBlank(message = "Subject is Mandatory")
	@Size(min = 10, max = 500,message = "Phone number should be 10 digit")
	private String subject;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getL_name() {
		return l_name;
	}
	public void setL_name(String l_name) {
		this.l_name = l_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
}
