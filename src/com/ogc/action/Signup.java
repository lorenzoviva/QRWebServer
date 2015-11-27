package com.ogc.action;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ogc.facades.QRUserFacade;
import com.ogc.model.QRUser;
import com.ogc.utility.GsonHelper;

public class Signup extends Action {

	public Signup() {
		super(null);
	}

	@Override
	public JsonObject perform(JSONObject parameters) {

		try {
			String firstName = parameters.getString("firstname");
			String lastName = parameters.getString("lastname");
			String text = parameters.getString("text");
			String password = parameters.getString("password");
			boolean useQrPassword = parameters.getBoolean("useQrPassword");
 
			QRUserFacade qruserfacade = new QRUserFacade();

			QRUser qruser = null;
			try {
				qruser = qruserfacade.createQRUser(firstName, lastName, password, text, useQrPassword);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (qruser == null) {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;

			} else {
				Gson gson = GsonHelper.customGson;
				// creates json from country object
				JsonElement userObj = gson.toJsonTree(qruser);

				// create a new JSON object
				JsonObject myObj = new JsonObject();
				// add property as success
				myObj.addProperty("success", true);
				// add the country object
				myObj.add("QRUser", userObj);
				// convert the JSON to string and send back
				return myObj;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		return (!parameters.has("QRSquare") && !parameters.has("user"));
	}
	

}
