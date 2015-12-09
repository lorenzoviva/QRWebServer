package com.ogc.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.codehaus.jettison.json.JSONObject;

@Entity
@DiscriminatorValue(value = "QRUserMenager")
public class QRUserMenager extends QRWebPage{

	@Column(length = 2000)
	private String password;

	

	public QRUserMenager(String html) {
		super(html);
	}

	public QRUserMenager() {
		super();
	}

	public QRUserMenager(String text,String html,String password) {
		super(text,html);
		this.password = password;
	}

	public QRUserMenager(String text, String html) {
		super(text,html);
	}

	public String getPassword() {
		return password;
	}

	public void setHtml(String html){
		super.setHtml(html);
	}
	public String getHtml(){
		return super.getHtml();
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public boolean canBeCreated(JSONObject parameters){
		return false;
	}

	
}
