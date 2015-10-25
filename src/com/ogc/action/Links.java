package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.utility.GsonHelper;

public class Links extends Action{

	public Links() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("QRSquare") || !parameters.has("type")) {
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
						List<QRSquareUser> squareUser = gson.fromJson(parameters.get("QRSquareUser"), listType);
						for (int i = 0; i < squareUser.size(); i++) {

							if (squareUser.get(i).getRole() != null) {
								switch (squareUser.get(i).getRole().getName().toLowerCase()) {
								case "owner":
									return true;
								case "menager":
									return true;
								case "writer":
									return true;
								case "isolatedwriter":
									return true;
								case "reader":
									return true;
								case "isolatedreader":
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
