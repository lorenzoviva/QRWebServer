package com.ogc.action;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ogc.facades.QRSquareFacade;
import com.ogc.model.QRFreeDraw;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;

public class Savedraw extends Action{
	
	public Savedraw() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonObject perform(JSONObject parameters) {

		QRSquareFacade squarefacade = new QRSquareFacade();
		String error = "";
		QRFreeDraw fd = null;
		try {
			if (parameters.has("qrfreedraw")) {
				fd = (new Gson()).fromJson(parameters.getJSONObject("qrfreedraw").toString(), QRFreeDraw.class);
				squarefacade.save(fd);
			} else {
				error = "there is nothing to save";
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

			if (fd != null) {
				// creates json from country object
				JsonElement squareObj = gson.toJsonTree(fd);

				// create a new JSON object
				// add property as success
				myObj.addProperty("success", true);
				// add the QRSquare object
				try {
					parameters.put("type", fd.getClass().getName());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		
				myObj.addProperty("type", fd.getClass().getName());
				
				// if
				// (!square.getClass().getName().equals(QRUserMenager.class.getName())
				// || !possibleActions.contains("login")) {
				myObj.add("QRSquare", squareObj);
		
				// } else {
				// myObj.add("QRSquare", gson.toJsonTree(new
				// QRPreLoginPage(square.getText())));
				// }
				// convert the JSON to string and send back
				return myObj;
			} else {
				// add property as success
				myObj.addProperty("success", false);
				error = "there is nothing to save";
				myObj.addProperty("error", error);
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
