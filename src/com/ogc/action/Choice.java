package com.ogc.action;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ogc.model.QRSquare;
import com.ogc.utility.GsonHelper;

public class Choice extends Action {

	private static Class[] subactionarray =  {Back.class};
	@SuppressWarnings("unchecked")
	public Choice() {
		super(subactionarray);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		return false;
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		String error = "";
		String choises = "";
		if (parameters.has("from")) {
			try {
				String from = parameters.getString("from");
				
				if (from.equals("create")) {
					List<Class<? extends QRSquare>> qrsquaretypes = QRSquare.getExtedingClasses();
					for (int i = 0; i < qrsquaretypes.size(); i++) {
						if (qrsquaretypes.get(i).newInstance().canBeCreated(parameters)) {
							choises += qrsquaretypes.get(i).getSimpleName()+",";
						}
					}
				} else {
					error += "choose what?";
				}

			} catch (JSONException | InstantiationException | IllegalAccessException e) {
				error += "there are no enough parameters";
			}
			
			

		}else{
			error += "choose what?";
		}
		JsonObject myObj = new JsonObject();
		if (!error.equals("")) {
			System.out.println("error :" + error);
			myObj.addProperty("success", false);
			
			return myObj;

		} else {
			Gson gson = GsonHelper.customGson;
			// creates json from country object

			// create a new JSON object
			// add property as success
			myObj.addProperty("success", true);
			// add the country object
			String possibleActions = getPossibleActions(myObj);
			myObj.addProperty("action", possibleActions);
			myObj.addProperty("choises", choises);
			// convert the JSON to string and send back
			return myObj;
		}

	}

}
