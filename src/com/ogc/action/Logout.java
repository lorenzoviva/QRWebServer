package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ogc.model.QRSquareUser;

public class Logout extends Action {

	public Logout() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("QRSquare") || !parameters.has("type") || !parameters.has("user") || !parameters.has("QRSquareUser")) {
			return false;
		} else {
			String type;
			type = parameters.get("type").getAsString();
			if (type.endsWith("QRUserMenager")) {				
				Type listType = new TypeToken<ArrayList<QRSquareUser>>() {
				}.getType();
				List<QRSquareUser> squareUsers = (new Gson()).fromJson(parameters.get("QRSquareUser"), listType);
				for (int i = 0; i < squareUsers.size(); i++) {
					QRSquareUser squareUser = squareUsers.get(i);
					if (squareUser.getRole() != null) {
						if(squareUser.getRole().getName().toLowerCase().equals("owner")) {
							return true;
						}
						
					}

				}
				return false;
//				QRSquareUser squareUser = (new Gson()).fromJson(parameters.getAsJsonObject("QRSquareUser"), QRSquareUser.class);
//				return squareUser.getRole().getName().equals("owner");

			} else {
				return false;
			}

		}
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		return null;
	}

}
