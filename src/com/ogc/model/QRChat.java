package com.ogc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;

@Entity
@DiscriminatorValue(value="QRChat")

public class QRChat extends QRSquare {
	
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="chat_id")
	List<QRMessage> messages;
	
	@Override
	public boolean canBeCreated(JSONObject parameters) {
		return true;
	}
	
	public QRChat() {
		super();
	}

	public QRChat(JSONObject jobj) throws JSONException {
		this.setText(jobj.getString("text"));
		this.setCreationDate(new Gson().fromJson(jobj.getString("creationDate"), Date.class));
		this.setVisit(jobj.getLong("visit"));
		this.setAcl(new ACL(jobj.getJSONObject("acl")));
		JSONArray jsonarray = jobj.getJSONArray("messages");
		messages=new ArrayList<QRMessage>();
		for (int i=0;i<jsonarray.length();i++) {
			messages.add(new QRMessage(jsonarray.getJSONObject(i)));
		}
	}
	
	public JSONObject toJSONObject(){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("text", this.getText());
		String jsonDate = (new Gson()).toJson(this.getCreationDate(),Date.class);
		jsonDate.replace("\"", "");
		jsonMap.put("creationDate", jsonDate);
		jsonMap.put("visit", this.getVisit());
		jsonMap.put("acl", this.getAcl().toJSON());
		JSONArray array = new JSONArray();
		for(int i = 0 ; i < messages.size();i++){
			array.put(messages.get(i).toJSONObject());
		}
		jsonMap.put("messages", array);
		return new JSONObject(jsonMap);
	}

	public List<QRMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<QRMessage> messages) {
		this.messages = messages;
	}
}
