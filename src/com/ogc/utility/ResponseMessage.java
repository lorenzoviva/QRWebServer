package com.ogc.utility;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

public class ResponseMessage {
	private String message;
	private Integer code;
	private JSONObject object;
	
	public JSONObject getJSONResponse(){
		Map<String,Object> jsonmap = new HashMap<String,Object>();
		jsonmap.put("mesasge", message);
		jsonmap.put("code", code);
		jsonmap.put("object",object);
		JSONObject result = new JSONObject(jsonmap);
		return result;
	}
	
	public ResponseMessage(JSONObject object) {
		this.object = object;
		message = "";
		code = 0;
	}
	public ResponseMessage(String message, Integer code) {
		this.message = message;
		this.code = code;
		object = null;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public JSONObject getObject() {
		return object;
	}
	public void setObject(JSONObject object) {
		this.object = object;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		ResponseMessage other = (ResponseMessage) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}
	
	
	
	
	
	
}
