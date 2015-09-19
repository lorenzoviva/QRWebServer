package com.ogc.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "QRUserMenager")
public class QRUserMenager extends QRSquare{
	@Column(length = 2000)
	private String html;
	@Column(length = 2000)
	private String password;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public QRUserMenager(String html) {
		super();
		this.html = html;
	}

	public QRUserMenager() {
		super();
	}

	public QRUserMenager(String text,String html,String password) {
		super(text);
		this.html = html;
		this.password = password;
	}

	public QRUserMenager(String text, String html) {
		super(text);
		this.html = html;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
}
