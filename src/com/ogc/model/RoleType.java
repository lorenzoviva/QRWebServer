package com.ogc.model;

import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoleType {
	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	@Column(nullable = false, length = 100, unique = true)
	private String name;
	@ElementCollection
	private List<String> qrSquareClasses;
	
	public RoleType() {
	}
	public RoleType(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addQRSquareClass(String className){
		if(qrSquareClasses== null || qrSquareClasses.equals(null) || qrSquareClasses.isEmpty()){
			qrSquareClasses = new ArrayList<String>();
		}
		qrSquareClasses.add(className);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleType other = (RoleType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public List<String> getQrSquareClasses() {
		return qrSquareClasses;
	}
	public void setQrSquareClasses(List<String> qrSquareClasses) {
		this.qrSquareClasses = qrSquareClasses;
	}
	
	
	
}
