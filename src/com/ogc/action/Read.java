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

public class Read extends Action {

	public Read() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("QRSquare") || !parameters.has("type")) {
			return false;
		} else {
			if (!parameters.has("user")) {
				String type = parameters.get("type").getAsString();
				Gson gson = GsonHelper.customGson;
				QRSquare square;
				if(type.endsWith("QRUserMenager") && !parameters.has("users")){
					return true;
				}
				try {
					square = (QRSquare) gson.fromJson(parameters.getAsJsonObject("QRSquare"), Class.forName(type));
					if (square.getAcl().getRead()) {
						return true;
					}
				} catch (JsonSyntaxException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				return false;
			} else {
				if (!parameters.has("QRSquareUser")) {
					String type = parameters.get("type").getAsString();
					Gson gson = GsonHelper.customGson;
					QRSquare square;
					try {
						square = (QRSquare) gson.fromJson(parameters.getAsJsonObject("QRSquare"), Class.forName(type));
						if (square.getAcl().getRead()) {
							return true;
						}
					} catch (JsonSyntaxException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					return false;
				} else {
					Type listType = new TypeToken<ArrayList<QRSquareUser>>() {
					}.getType();
					List<QRSquareUser> qrSquareUser = GsonHelper.customGson.fromJson(parameters.get("QRSquareUser"), listType);
					for (int i = 0; i < qrSquareUser.size(); i++) {
						String roleName = qrSquareUser.get(i).getRole().getName().toLowerCase();
						if (!roleName.startsWith("request")) {
							return true;
						}
					}
					try {
						Gson gson = GsonHelper.customGson;
						QRSquare square;
						String type = parameters.get("type").getAsString();
						square = (QRSquare) gson.fromJson(parameters.getAsJsonObject("QRSquare"), Class.forName(type));
						if (square.getAcl().getRead()) {
							return true;
						}
					} catch (JsonSyntaxException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					return false;

				}
			}

		}
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
