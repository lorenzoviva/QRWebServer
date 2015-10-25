package com.ogc.action;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;

public class Chat extends Action{

	public Chat() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("QRSquare") || !parameters.has("type")) {
			return false;
		}else{
			String type;
			type = parameters.get("type").getAsString();
			if (type.endsWith("QRChat")) {
				return true;
			}else{
				return false;
			}
		}
			
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
