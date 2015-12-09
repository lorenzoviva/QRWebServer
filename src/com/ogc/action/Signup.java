package com.ogc.action;

import java.util.List;

import javafx.util.Pair;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ogc.facades.QRSquareFacade;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.facades.QRUserFacade;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.utility.GsonHelper;

public class Signup extends Action {
	private static Class[] subactionarray = { Create.class, Edit.class, Link.class, Links.class, Load.class, Login.class, Logout.class, Request.class, Signup.class, Users.class, Chat.class,Read.class };
	
	public Signup() {
		super(subactionarray);
	}

	@Override
	public JsonObject perform(JSONObject parameters) {

		try {
			String firstName = parameters.getString("firstname");
			String lastName = parameters.getString("lastname");
			String text = parameters.getString("text");
			String password = parameters.getString("password");
			boolean useQrPassword = parameters.getBoolean("useQrPassword");
 
			QRUserFacade qruserfacade = new QRUserFacade();
			QRSquareFacade qrsquarefacade = new QRSquareFacade();
			QRSquareUserFacade qrsquareuserfacade = new QRSquareUserFacade();

			QRUser qruser = null;
			QRSquare qrsquare = null;
			List<QRSquareUser> qrsquareuser = null;
			
			try {
				Pair<QRUser, QRSquare> pair = qruserfacade.createQRUser(firstName, lastName, password, text, useQrPassword);
				qruser = pair.getKey();
				qrsquare = pair.getValue();
				qrsquareuser = qrsquareuserfacade.getQRSquareUser(text, qruser.getId());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (qruser == null || qrsquare == null || qrsquareuser == null) {
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
				myObj.add("user", userObj);
				// convert the JSON to string and send back
				myObj.add("QRSquare", gson.toJsonTree(qrsquare));
				myObj.addProperty("type", qrsquare.getClass().getName());
				myObj.add("QRSquareUser", gson.toJsonTree(qrsquareuser));
				myObj.addProperty("action", getPossibleActions(myObj).toLowerCase().replace("read,", ""));
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
		return (!parameters.has("QRSquare") && !parameters.has("user"));
	}
	

}
