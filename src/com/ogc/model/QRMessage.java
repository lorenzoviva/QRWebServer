package com.ogc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

@Entity
public class QRMessage {
	
	@Column (nullable=false)
	private String text;
	@Column
	private Date date;
	@OneToOne (fetch=FetchType.LAZY, cascade={CascadeType.PERSIST})
	private QRUser sender;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	public QRMessage(String text, QRUser sender) {
		this.text = text;
		this.date = new Date();
		this.sender = sender;
	}
	public QRMessage(String text) {
		this.text = text;
		this.date = new Date();
		this.sender = null;
	}
	public QRMessage() {
	}
	
	public QRMessage(JSONObject jobj) throws JSONException {
		this.setText(jobj.getString("text"));
		this.setDate(new Gson().fromJson(jobj.getString("date"), Date.class));
		this.setSender(new Gson().fromJson(jobj.getString("sender"), QRUser.class));
		this.setId(jobj.getLong("id"));
	}
	
	public JSONObject toJSONObject(){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("text", this.getText());
		jsonMap.put("date", (new Gson()).toJson(this.getDate(),Date.class));
		jsonMap.put("id", this.getId());
		jsonMap.put("sender", (new Gson()).toJsonTree(this.getSender(),QRUser.class));
		return new JSONObject(jsonMap);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public QRUser getSender() {
		return sender;
	}
	public void setSender(QRUser sender) {
		this.sender = sender;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
		QRMessage other = (QRMessage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "QRMessage [text=" + text + ", date=" + date + "]";
	}
}
