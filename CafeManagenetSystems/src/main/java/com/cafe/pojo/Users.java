package com.cafe.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Users.findByEmailId", query = "select u from Users u where u.email=:email")

@NamedQuery(name = "Users.getAllUser", query = "select new com.cafe.wrapper.UserWrapper(u.id, u.name,u.contactNumber,u.email,u.status) from Users u where u.role='user'")

@NamedQuery(name = "Users.updateStatus", query = "update Users u set u.status=:status where u.id=:id")

@NamedQuery(name = "Users.getAllAdmin", query = "select u.email from Users u where u.role='admin'")


@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "CAFE-USERS")
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String contactNumber;
	private String email;
	private String password;
	private String status;
	private String role;
	public Users(Integer id, String name, String contactNumber, String email, String password, String status,
			String role) {
		super();
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
		this.email = email;
		this.password = password;
		this.status = status;
		this.role = role;
	}
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
