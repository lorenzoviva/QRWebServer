package com.ogc.action;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;
import com.ogc.facades.QRSquareFacade;

public class Savesquareuser extends Action{

	public Savesquareuser() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		return false;
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		JsonObject myObj = new JsonObject();
		try {
			String lastrequest = parameters.getString("lastrequest");
			String newrole = parameters.getString("newrole");
			JsonObject choiseRequest = (new Choice()).perform(new JSONObject(lastrequest));
			if(choiseRequest.get("s").getAsBoolean()){
				String choises = choiseRequest.get("choises").getAsString();
				if(choises.contains(newrole)){
					QRSquareFacade squareuserfacade = new QRSquareFacade();
					
					myObj.addProperty("success", true);
				}else{
					myObj.addProperty("success", false);
				}
			}
		} catch (JSONException e) {
			myObj.addProperty("success", false);
		}
		
		
		return myObj;
	}

}
