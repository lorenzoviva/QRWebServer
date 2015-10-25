package com.ogc.action;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ogc.facades.QRUserFacade;
import com.ogc.model.QRUser;
import com.ogc.utility.GsonHelper;

public class Login extends Action {

	public Login() {
		super(null);
	}

	@Override
	public JsonObject perform(JSONObject parameters) {

		try {

			String text = parameters.getString("text");
			String password = parameters.getString("password");
			System.out.println(parameters.toString());
			QRUserFacade qruserfacade = new QRUserFacade();

			QRUser qruser = null;

			qruser = qruserfacade.checkQRUserMenager(text, password);

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
		if (!parameters.has("QRSquare") || !parameters.has("type") || parameters.has("user")) {
			return false;
		} else {
			String type;
			type = parameters.get("type").getAsString();
			if (type.endsWith("QRUserMenager")) {
				return true;
			} else {
				return false;
			}

		}
	}

}
