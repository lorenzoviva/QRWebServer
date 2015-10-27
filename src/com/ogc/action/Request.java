package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.utility.GsonHelper;

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
		if (!parameters.has("QRSquare") || !parameters.has("user")  || !parameters.has("type")) {
			return false;
		} else {
			if (!parameters.has("QRSquareUser")) {
				String type = parameters.get("type").getAsString();
				Gson gson = GsonHelper.customGson;
				QRSquare square;
				try {
					square = (QRSquare) gson.fromJson(parameters.getAsJsonObject("QRSquare"), Class.forName(type));
					if (square.getAcl().getWrite()) {
						return false;
					}				
				} catch (JsonSyntaxException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				return true;
			} else {
				Type listType = new TypeToken<ArrayList<QRSquareUser>>() {
				}.getType();
				List<QRSquareUser> qrSquareUser = GsonHelper.customGson.fromJson(parameters.get("QRSquareUser"), listType);
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
