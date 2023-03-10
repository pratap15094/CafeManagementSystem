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

@NamedQuery(name = "Category.getAllCatgory", query = "select c from Category c where c.id IN(select p.category from Product p where p.status='true')")

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "CATEGORYS")
public class Category implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    
	public Category(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Category() {
		super();
	}
	
	//getter and setter
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
}
