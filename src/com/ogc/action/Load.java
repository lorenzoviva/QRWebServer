package com.ogc.action;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogc.facades.QRSquareFacade;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.facades.QRUserFacade;
import com.ogc.facades.RoleTypeFacade;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.utility.GsonHelper;

public class Load extends Action {

	private static Class[] subactionarray = { Create.class, Edit.class, Link.class, Links.class, Load.class, Login.class, Logout.class, Request.class, Signup.class, Users.class, Chat.class,Read.class };

	@SuppressWarnings("unchecked")
	public Load() {
		super(subactionarray);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonObject perform(JSONObject parameters) {

		QRSquareFacade squarefacade = new QRSquareFacade();
		QRSquareUserFacade squareUserFacade = new QRSquareUserFacade();
		
		String error = "";
		long userid = -1;
		QRSquare square = null;
		List<QRSquareUser> squareUser = null;
		String text = "";
		try {

			if (parameters.has("text")) {
				text = parameters.getString("text");
				square = squarefacade.loadQRFromText(text);
				if (parameters.has("user")) {
					userid = parameters.getLong("user");
					try {
						squareUser = squareUserFacade.getQRSquareUser(text, userid);
					} catch (javax.persistence.NoResultException e) {
						squareUser = null;
					}
				}

			} else {
				error = "there is no text";
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

			if (square != null) {
				// creates json from country object
				JsonElement squareObj = gson.toJsonTree(square);

				// create a new JSON object
				// add property as success
				myObj.addProperty("success", true);
				// tells if the QRSquare object is already in db
				myObj.addProperty("free", false);

				
				if (squareUser != null && !squareUser.isEmpty()) {
					JsonElement squareUserJson = gson.toJsonTree(squareUser);
					myObj.add("QRSquareUser", squareUserJson);
				}

				myObj.addProperty("type", square.getClass().getName());

				myObj.add("QRSquare", squareObj);
				if (userid != -1) {
					myObj.addProperty("user", userid);
				}
				System.out.println("getPossibleAction(" + myObj.toString() + ")");
				String possibleActions = getPossibleActions(myObj);
				
				if(!(new Read()).canPerform(myObj)){//cannot be read
					myObj.remove("QRSquare");
					QRSquare empty = new QRSquare(square.getText());
					myObj.add("QRSquare", gson.toJsonTree(empty));
				}else if((squareUser == null || squareUser.isEmpty()) && userid != -1 && !(new Request()).canPerform(myObj)){//the logged user can read but does not have a squareuser with that square
					(new QRSquareUserFacade()).addQRSquareUserRead(square, userid);
				}
				//sending a list, separated by commas with all possible actions eg(action:"create,signup,")
				myObj.addProperty("action", possibleActions);
				//send back the JSON
				return myObj;
			} else {
				// add property as success
				myObj.addProperty("success", true);
				// tells if the QRSquare object is not in db
				myObj.addProperty("free", true);
				if (userid != -1) {
					myObj.addProperty("user", userid);
				}
				myObj.addProperty("text", text);
				String possibleActions = getPossibleActions(myObj);
				myObj.addProperty("action", possibleActions);
				// convert the JSON to string and send back
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
