package com.ogc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.codehaus.jettison.json.JSONObject;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="SQUARE_TYPE")

public class QRSquare {

	@Id
	private String text;
	@Column(nullable = false)
	private Date creationDate;
	@Column(nullable = false)
	private Long visit;
	@OneToOne
	private ACL acl;
	
	public QRSquare() {
		
	}	
	public static List<Class<? extends QRSquare>> getExtedingClasses(){
		List<Class<? extends QRSquare>> extendingClasses = new ArrayList<Class<? extends QRSquare>>();
		extendingClasses.add(QRFreeDraw.class);
		extendingClasses.add(QRUserMenager.class);
		extendingClasses.add(QRWebPage.class);
		extendingClasses.add(QRChat.class);
		return extendingClasses;
	}
	

	public QRSquare(String text) {
		this.text = text;
		this.visit = 1L;
		this.creationDate = new Date();
	}

	public String getText() {
		return text;
	}
	public boolean canBeCreated(JSONObject parameters){
		return true;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getVisit() {
		return visit;
	}

	public void setVisit(Long visit) {
		this.visit = visit;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		QRSquare other = (QRSquare) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public ACL getAcl() {
		return acl;
	}

	public void setAcl(ACL acl) {
		this.acl = acl;
	}

}
