package com.ogc.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.codehaus.jettison.json.JSONObject;

@Entity
@DiscriminatorValue(value="QRChat")

public class QRChat extends QRSquare {
	
	@Override
	public boolean canBeCreated(JSONObject parameters) {
		return true;
	}
	
	@Column(length = 500000)
	private String chat;

	public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}
}
