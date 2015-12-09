package com.ogc.action;

import java.lang.reflect.Type;
import java.util.Collection;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.ogc.facades.QRSquareFacade;
import com.ogc.model.QRWebPage;
import com.ogc.utility.GsonHelper;

public class Savepage extends Action{
	
	public Savepage() {
		super(null);
	}

	@Override
	public JsonObject perform(JSONObject parameters) {

		QRSquareFacade squarefacade = new QRSquareFacade();
		String error = "";
		Class<? extends QRWebPage> wpt;
		QRWebPage wp = null;
		try {
			if (parameters.has("jsonwebpage") && parameters.has("type")) {
				String type = parameters.getString("type");
				try {
					wpt = (Class<? extends QRWebPage>) Class.forName(type);
					wp = wpt.cast((GsonHelper.customGson).fromJson(parameters.getString("jsonwebpage"), wpt));
				} catch (JsonSyntaxException | ClassNotFoundException e) {
					wp = (GsonHelper.customGson).fromJson(parameters.getString("jsonwebpage"), QRWebPage.class);
				}

				if(wp!=null){
					if(parameters.has("userid")){
						long user = parameters.getLong("userid");
						squarefacade.save(wp,user);
					}else{
						squarefacade.save(wp,-1);
					}
				}
				
			} else {
				error = "there is nothing to save";
			}
		} catch (JSONException | IllegalArgumentException | SecurityException e) {
			error = "error :" + e.getMessage();
			e.printStackTrace();
		}

		JsonObject myObj = new JsonObject();

		if (!error.equals("")) {

			myObj.addProperty("success", false);
			myObj.addProperty("error", error);
			return myObj;
		} else {
			Gson gson = GsonHelper.customGson;

			if (wp != null) {
				// creates json from country object
				JsonElement squareObj = gson.toJsonTree(wp);

				// create a new JSON object
				// add property as success
				myObj.addProperty("success", true);
				// add the QRSquare object
				try {
					parameters.put("type", wp.getClass().getName());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		
				myObj.addProperty("type", wp.getClass().getName());
				myObj.add("QRSquare", squareObj);
				return myObj;
			} else {
				// add property as success
				myObj.addProperty("success", false);
				error = "there is nothing to save";
				myObj.addProperty("error", error);
				return myObj;
			}

		}
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
