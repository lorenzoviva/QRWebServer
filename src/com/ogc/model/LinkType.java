package com.ogc.model;

import static javax.persistence.GenerationType.AUTO;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class LinkType {
	
	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	@Column(nullable = false,length = 100, unique = true)
	private String name;
	@ElementCollection
	private Map<String,String> acceptableConnections;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private LinkType opposite;
	
	
	
	
	
	
	public LinkType() {

	}
	public LinkType(String name, Map<String,String> acceptableConnections, LinkType opposite) {
		this.name = name;
		this.acceptableConnections = acceptableConnections;
		this.opposite = opposite;
	}
	public LinkType(String name, LinkType opposite) {
		this.name = name;
		this.opposite = opposite;
	}
	public LinkType(String name) {
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

	public LinkType getOpposite() {
		return opposite;
	}
	public void setOpposite(LinkType opposite) {
		this.opposite = opposite;
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
		LinkType other = (LinkType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Map<String, String> getAcceptableConnections() {
		return acceptableConnections;
	}
	public void setAcceptableConnections(Map<String, String> acceptableConnections) {
		this.acceptableConnections = acceptableConnections;
	}
	
	
}
