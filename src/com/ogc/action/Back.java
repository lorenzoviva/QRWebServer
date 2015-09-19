package com.ogc.action;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;

public class Back extends Action{

	public Back() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		return true;
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
