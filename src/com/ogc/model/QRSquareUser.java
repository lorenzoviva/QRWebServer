package com.ogc.model;

import static javax.persistence.GenerationType.AUTO;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
@Entity
public class QRSquareUser {
	
	@Id
	@GeneratedValue(strategy = AUTO)
	private long id;
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private QRSquare square;
	@ManyToOne(fetch = FetchType.EAGER)
	private QRUser user;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private RoleType role;
	@Column(nullable = false)
	private Date date;
	
	
	public QRSquareUser() {
	}
	public QRSquareUser(QRSquare square, QRUser user, RoleType role) {
		
		this.square = square;
		this.user = user;
		this.role = role;
		this.date = new Date();
	}
	public QRSquare getSquare() {
		return square;
	}
	public void setSquare(QRSquare square) {
		this.square = square;
	}
	public QRUser getUser() {
		return user;
	}
	public void setUser(QRUser user) {
		this.user = user;
	}
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
		QRSquareUser other = (QRSquareUser) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
	
}
