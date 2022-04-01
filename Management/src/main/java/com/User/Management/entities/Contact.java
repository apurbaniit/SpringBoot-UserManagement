package com.User.Management.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author RONY
 *
 */
@Entity
@Table(name="CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	private String f_name;
	private String l_name;
	private String work;
	private String email;
	@NotBlank(message = "Phone Number is Mandatory")
	@Size(min = 10, max = 10,message = "Phone number should be 10 digit")
	private String phone;
	private String imageUrl;
	@Column(length = 5000)
	private String information;
	
	@ManyToOne()
	private User user;


	public int getCid() {
		return cid;
	}


	public void setCid(int cid) {
		this.cid = cid;
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


	public String getWork() {
		return work;
	}


	public void setWork(String work) {
		this.work = work;
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


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getInformation() {
		return information;
	}


	public void setInformation(String information) {
		this.information = information;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	/*
	 * @Override public String toString() { return "Contact [cid=" + cid +
	 * ", f_name=" + f_name + ", l_name=" + l_name + ", work=" + work + ", email=" +
	 * email + ", phone=" + phone + ", imageUrl=" + imageUrl + ", information=" +
	 * information + ", user=" + user + "]"; }
	 */
	
	@Override
	public boolean equals(Object obj) {
		 
		return this.cid==((Contact)obj).getCid();
	}
	
	
	
	
}
