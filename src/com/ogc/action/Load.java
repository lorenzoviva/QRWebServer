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
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;

public class Load extends Action {

	
	


	private static Class[] subactionarray =  {Create.class,Edit.class,Link.class, Links.class ,Load.class,Login.class,Logout.class,Request.class ,Signup.class,Users.class};
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
		QRSquare square = null;
		List<QRSquareUser> squareUser = null;
		try {

			if (parameters.has("text")) {
				String text = parameters.getString("text");
				square = squarefacade.getQRFromText(text);
				if (parameters.has("user")) {
					long userid = parameters.getLong("user");
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
			Gson gson = new Gson();

			if (square != null) {
				// creates json from country object
				JsonElement squareObj = gson.toJsonTree(square);

				// create a new JSON object
				// add property as success
				myObj.addProperty("success", true);
				// add the QRSquare object
				myObj.addProperty("free", false);
				try {
					parameters.put("type", square.getClass().getName());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				if (squareUser != null) {
					JsonElement squareUserJson = gson.toJsonTree(squareUser);
					
					myObj.add("QRSquareUser", squareUserJson);
				}

		
				myObj.addProperty("type", square.getClass().getName());
				
				// if
				// (!square.getClass().getName().equals(QRUserMenager.class.getName())
				// || !possibleActions.contains("login")) {
				myObj.add("QRSquare", squareObj);
				System.out.println("getPossibleAction(" + myObj.toString() + ")");
				if (parameters.has("user")) {
					long userid;
					try {
						userid = parameters.getLong("user");
						myObj.addProperty("user", userid);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String possibleActions = getPossibleActions(myObj);
				myObj.addProperty("action", possibleActions);
				// } else {
				// myObj.add("QRSquare", gson.toJsonTree(new
				// QRPreLoginPage(square.getText())));
				// }
				// convert the JSON to string and send back
				return myObj;
			} else {
				// add property as success
				myObj.addProperty("success", true);
				// add the country object
				myObj.addProperty("free", true);
				JsonParser parser = new JsonParser();
				JsonObject parobject = (JsonObject) parser.parse(parameters.toString());
				String possibleActions = getPossibleActions(parobject);
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
