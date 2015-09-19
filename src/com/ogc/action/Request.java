package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.model.QRSquareUser;

public class Request extends Action {

	public Request() {
		super(null);
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		try {
			String text = parameters.getString("text");
			long userid = parameters.getLong("user");

			QRSquareUserFacade qrSquareUserfacade = new QRSquareUserFacade();

			QRSquareUser squareUser = qrSquareUserfacade.requestSquareUser(text, userid);

			if (squareUser == null) {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;

			} else {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", true);
				return myObj;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", false);
		return myObj;
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("QRSquare") || !parameters.has("user")) {
			return false;
		} else {
			if (!parameters.has("QRSquareUser")) {
				return true;
			} else {
				Type listType = new TypeToken<ArrayList<QRSquareUser>>() {
				}.getType();
				List<QRSquareUser> qrSquareUser = (new Gson()).fromJson(parameters.get("QRSquareUser"), listType);
				for (int i = 0; i < qrSquareUser.size(); i++) {
					String roleName = qrSquareUser.get(i).getRole().getName().toLowerCase();
					if (roleName.startsWith("request") || roleName.startsWith("owner")) {
						return false;
					}
				}
				return true;

			}

		}
	}

}
