package com.ogc.model;

import static javax.persistence.GenerationType.AUTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@Entity
public class QRUser {

	public QRUser() {
	}

	@Id
	@GeneratedValue(strategy = AUTO)
	private long id;
	@Column(length = 100)
	private String firstName;
	@Column(length = 100)
	private String lastName;
	@Column(nullable = false)
	private Date registrationDate;
	@Column(nullable = false)
	boolean anonymous;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE })
	private List<QRSquareUser> squares;
	@Column
	boolean useQrPassword;

	public long getId() {
		return id;
	}

	public QRUser(String firstName, String lastName, boolean useQrPassword) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.useQrPassword = useQrPassword;
		registrationDate = new Date();
		anonymous = (firstName.equals(null) && lastName.equals(null));

	}

	public QRUser(boolean anonymous) {
		this.anonymous = anonymous;
		registrationDate = new Date();
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String param) {
		this.firstName = param;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String param) {
		this.lastName = param;
	}

	public void setRegistrationDate(Date date) {
		this.registrationDate = date;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public List<QRSquareUser> getSquares() {
		return squares;
	}

	public void setSquares(List<QRSquareUser> squares) {
		this.squares = squares;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		QRUser other = (QRUser) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public JSONObject getJSON() {
		Map<String, Object> jsonmap = new HashMap<String, Object>();
		jsonmap.put("id", id);
		jsonmap.put("firstName", firstName);
		jsonmap.put("lastName", lastName);
		jsonmap.put("registrationDate", registrationDate);
		jsonmap.put("anonymous",anonymous);
		jsonmap.put("useQrPassword", useQrPassword);
		JSONObject result = new JSONObject(jsonmap);
		return result;

	}

}