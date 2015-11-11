package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.model.QRChat;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.utility.GsonHelper;

public class Edit extends Action {

	public Edit() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("QRSquare") || !parameters.has("type") || (!parameters.has("users") && parameters.get("type").getAsString().endsWith(QRChat.class.getName()))) {
			return false;
		} else {
			String type = parameters.get("type").getAsString();
			Gson gson = GsonHelper.customGson;
			try {
				QRSquare square = (QRSquare) gson.fromJson(parameters.getAsJsonObject("QRSquare"), Class.forName(type));
				if (square.getAcl().getWrite()) {
					return true;
				} else {
					if (!parameters.has("QRSquareUser")) {
						return false;
					} else {
						Type listType = new TypeToken<ArrayList<QRSquareUser>>() {
						}.getType();
						List<QRSquareUser> squareUsers = gson.fromJson(parameters.get("QRSquareUser"), listType);
						for (int i = 0; i < squareUsers.size(); i++) {
							QRSquareUser squareUser = squareUsers.get(i);
							if (squareUser.getRole() != null) {
								switch (squareUser.getRole().getName().toLowerCase()) {
								case "owner":
									return true;
								case "menager":
									return true;
								case "writer":
									return true;
								case "mutedwriter":
									return true;
								case "isolatedwriter":
									return true;
								case "onlywriter":
									return true;
								case "request":
									break;
								default:
									return false;
								}
							}

						}
						return false;

					}
				}
			} catch (JsonSyntaxException e) {
				return false;
			} catch (ClassNotFoundException e) {
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
