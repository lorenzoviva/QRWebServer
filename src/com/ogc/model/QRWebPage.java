package com.ogc.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.codehaus.jettison.json.JSONObject;

@Entity
@DiscriminatorValue(value = "QRWebPage")
public class QRWebPage extends QRSquare{


	@Column(length = 2000)
	public String html;
	

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public QRWebPage(String html) {
		super();
		this.html = html;
	}

	public QRWebPage() {
		super();
	}

	public QRWebPage(String text,String html) {
		super(text);
		this.html = html;
	}
	@Override
	public boolean canBeCreated(JSONObject parameters){
		return true;
	}
}
