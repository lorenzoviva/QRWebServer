package com.ogc.action;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;

public class Change extends Action{

	public Change() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
