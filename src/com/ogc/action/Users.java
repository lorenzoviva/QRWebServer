package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;

public class Users extends Action {
	
	private static Class[] subactionarray =  {Add.class,Change.class,Remove.class,Back.class};
	@SuppressWarnings("unchecked")
	public Users() {
		super(subactionarray);
	}

	public Users(Class<? extends Action>[] subactions) {
		super(subactions);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JsonObject perform(JSONObject parameters) {
		try {
			String text = parameters.getString("text");
			long userid = parameters.getLong("user");

			QRSquareUserFacade qrSquareUserfacade = new QRSquareUserFacade();

			List<QRSquareUser> squareUsers = qrSquareUserfacade.getSquareUsers(text);

			if (squareUsers == null || squareUsers.isEmpty()) {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;

			} else {
				Gson gson = new Gson();
				// creates json from country object
				JsonElement squareUsersObj = gson.toJsonTree(squareUsers);
				JsonElement squareObj = gson.toJsonTree(squareUsers.get(0).getSquare());
				JsonElement userObj = gson.toJsonTree(squareUsers.get(0).getUser());
				// create a new JSON object
				JsonObject myObj = new JsonObject();
				// add property as success
				myObj.addProperty("success", true);
				// add the country object
				myObj.add("QRSquareUser", squareUsersObj);
				myObj.add("QRSquare", squareObj);
				myObj.add("user", userObj);
				String possibleActions = getPossibleActions(myObj);
				myObj.addProperty("action", possibleActions);
				// convert the JSON to string and send back
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
		if (!parameters.has("QRSquare") || !parameters.has("type")) {
			return false;
		} else {
			String type = parameters.get("type").getAsString();
			Gson gson = new Gson();
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
						List<QRSquareUser> squareUser = (new Gson()).fromJson(parameters.get("QRSquareUser"), listType);
						for (int i = 0; i < squareUser.size(); i++) {

							if (squareUser.get(i).getRole() != null) {
								switch (squareUser.get(i).getRole().getName().toLowerCase()) {
								case "owner":
									return true;
								case "menager":
									return true;
								case "writer":
									return true;
								case "mutedwriter":
									return true;
								case "reader":
									return true;
								case "mutedreader":
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

}
