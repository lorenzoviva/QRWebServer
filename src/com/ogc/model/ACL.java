package com.ogc.model;

import static javax.persistence.GenerationType.AUTO;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Entity
public class ACL {
	
	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	@Column(nullable = false)
	private Boolean read;
	@Column(nullable = false)
	private Boolean write;
	
	
	
	public ACL() {
		
	}
	
	
	public ACL(Boolean read, Boolean write) {
		super();
		this.read = read;
		this.write = write;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
	public Boolean getWrite() {
		return write;
	}
	public void setWrite(Boolean write) {
		this.write = write;
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
		ACL other = (ACL) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public ACL(JSONObject obj){
		try {
			read = obj.getBoolean("read");
			write = obj.getBoolean("write");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public JSONObject toJSON() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("read", read);
		map.put("write", write);
		return new JSONObject(map);
	}
}
