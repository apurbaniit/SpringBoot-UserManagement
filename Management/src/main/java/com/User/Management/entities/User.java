package com.User.Management.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name="USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "Name Filed is Mandatory")
	@Size(min = 2, max = 20,message = "Min 2 character and max 20 character")
	private String name;
	
	@NotBlank(message = "Email Filed is Mandatory")
	@Column(unique = true)
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",message = "Invalid Email")
	private String email;
	
	@Column(length = 1200)
	@NotBlank(message = "Password Filed is Mandatory")
	//@Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$",message = "Invalid Password Pattern")
    //@Size(min = 8 ,max= 20,message = "---#One Lower Case #One Upper Case  #One Digit  #No WhiteSpace #Min 8 Max 20 Character---")
	@Size(min = 6, message = "password must be minimum 6 character")
	private String password;
	
	private String role;
	private boolean enabled;
	private String imagesUrl;
	
	@Column(length = 500)
	@NotBlank(message = "Name Filed is Mandatory")
	@Size(min = 10 ,max= 100,message = "Min 10 chracter and max 100 character")
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user", orphanRemoval=true)
	private List<Contact> contacts=new ArrayList<>();
	
	 
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImagesUrl() {
		return imagesUrl;
	}
	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imagesUrl=" + imagesUrl + ", about=" + about + ", contacts=" + contacts
				+ "]";
	}
	
	
	
}
