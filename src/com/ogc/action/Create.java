package com.ogc.action;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ogc.facades.ACLFacade;
import com.ogc.facades.InvalidRoleException;
import com.ogc.facades.QRSquareFacade;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Create extends Action {

	public Create() {
		super(null);
	}

	public Map<String, Object> getParameters(JSONObject json) throws JSONException {
		if (json.has("owner")) {
			json.remove("owner");
		}
		if (json.has("classname")) {
			json.remove("classname");
		}
		if (json.has("action")) {
			json.remove("action");
		}
		JSONArray names = json.names();
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < names.length(); i++) {
			String param = (String) names.get(i);
			result.put(param, json.get(param));
			System.out.println("parameter name:" + param);
		}
		return result;
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		String error = "";
		QRSquareFacade squarefacade  =  new QRSquareFacade();
		QRSquare square = null;
		try {
			if (parameters.has("classname")) {
				String className = parameters.getString("classname");
				if (parameters.has("acl")) {
					ACLFacade aclfacade = new ACLFacade();
					// ACL acl =
					// aclfacade.createACL(parameters.getJSONObject("acl"));
					ACL acl = new ACL(parameters.getJSONObject("acl"));
					aclfacade.saveACL(acl);
					parameters.put("acl", acl);
				}
				if (parameters.has("owner")) {
					QRUser owner = (QRUser) parameters.get("owner");
					Map<String, Object> param= getParameters(parameters);
					try {
						square = squarefacade.createNewQRSquare(className, param, owner);
					} catch (InvalidRoleException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Map<String, Object> param = getParameters(parameters);
					square = squarefacade.createNewQRSquare(className, param);
				}

			} else {
				error = "there is no classname";
			}
			System.out.println("JSONObject :" + parameters);
		} catch (JSONException | InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
			error = "error :" + e.getMessage();
			e.printStackTrace();
		}
		JsonObject myObj = new JsonObject();

		if (!error.equals("")) {
			myObj.addProperty("success", false);
			return myObj;

		} else {
			Gson gson = new Gson();
			// creates json from country object
			JsonElement squareObj = gson.toJsonTree(square);

			// create a new JSON object
			// add property as success
			myObj.addProperty("success", true);
			// add the country object
			myObj.add("QRSquare", squareObj);
			// convert the JSON to string and send back
			return myObj;
		}
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		return !parameters.has("QRSquare");
	}
}
