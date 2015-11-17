package com.ogc.action;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;
import com.ogc.facades.QRSquareFacade;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.facades.RoleTypeFacade;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.utility.GsonHelper;

public class Savesquareuser extends Action {

	public Savesquareuser() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		return false;
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		JsonObject myObj = new JsonObject();
		try {
			String lastrequest = parameters.getString("lastrequest");
			JSONObject lastobj = new JSONObject(lastrequest);
			JSONObject lastObj = lastobj;
			String newrole = parameters.getString("newrole");
			JsonObject choiseRequest = (new Choice()).perform(lastobj);
			if (choiseRequest.get("success").getAsBoolean()) {
				String choises = choiseRequest.get("choises").getAsString();
				if (choises.contains(newrole)) {
					long userid = lastobj.getLong("user");
					long otheruserid = lastobj.getLong("otheruser");
					String text = lastobj.getString("text");
					QRSquareUserFacade squareUserFacade = new QRSquareUserFacade();// (rolefacade.emf,rolefacade.em);
					// (squareUserFacade.emf, squareUserFacade.em);
					List<QRSquareUser> othUserSquareUsers = squareUserFacade.getQRSquareUser(text, otheruserid);
					String otheruserRole = "";
					QRSquareUser othersquareUser = null;

					if (othUserSquareUsers != null && !othUserSquareUsers.isEmpty()) {
						for (QRSquareUser othUserSquareUser : othUserSquareUsers) {
							if (otheruserRole.equals("") || Choice.isRoleGreater(othUserSquareUser.getRole().getName(), otheruserRole)) {
								otheruserRole = othUserSquareUser.getRole().getName();
								othersquareUser = othUserSquareUser;
							}
						}
					}
					if (newrole.equals("remove")) {
						squareUserFacade.remove(othersquareUser);
					} else {
						RoleTypeFacade rolefacade = new RoleTypeFacade();
						if (othersquareUser == null) {
							QRUser otheruser = squareUserFacade.getQRUser(otheruserid);
							QRSquare square = squareUserFacade.getQRSquare(text);
							othersquareUser = new QRSquareUser(square, otheruser, rolefacade.getRoleType(newrole));
						} else {
							othersquareUser.setRole(rolefacade.getRoleType(newrole));
						}
						rolefacade.close();
						squareUserFacade.save(othersquareUser);
					}

					myObj.addProperty("success", true);
				} else {
					myObj.addProperty("success", false);
				}
			}
		} catch (JSONException e) {
			myObj.addProperty("success", false);
		}

		return myObj;
	}

}
