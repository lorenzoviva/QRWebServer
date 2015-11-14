package com.ogc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ogc.facades.QRSquareFacade;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.utility.GsonHelper;

public class Choice extends Action {

	private static Class[] subactionarray = { Back.class };

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
		Map<String,String> otherParams = new HashMap<String,String>();
		if (parameters.has("from")) {
			try {
				String from = parameters.getString("from");

				if (from.equals("create")) {
					List<Class<? extends QRSquare>> qrsquaretypes = QRSquare.getExtedingClasses();
					for (int i = 0; i < qrsquaretypes.size(); i++) {
						if (qrsquaretypes.get(i).newInstance().canBeCreated(parameters)) {
							choises += qrsquaretypes.get(i).getSimpleName() + ",";
						}
					}
				} else if (from.equals("editrole")) {
					long userid = parameters.getLong("user");
					long otheruserid = parameters.getLong("otheruser");
					String text = parameters.getString("text");
					if (userid != -1 && otheruserid != -1 && !text.equals("")) {
						QRSquareUserFacade squareUserFacade = new QRSquareUserFacade();
						List<QRSquareUser> userSquareUsers = squareUserFacade.getQRSquareUser(text, userid);
						List<QRSquareUser> othUserSquareUsers = squareUserFacade.getQRSquareUser(text, otheruserid);
						QRSquare square = squareUserFacade.getQRSquare(text);
						QRUser otheruser = squareUserFacade.getQRUser(otheruserid);
						otherParams.put("user", GsonHelper.customGson.toJson(otheruser).toString()) ;
						otherParams.put("square", GsonHelper.customGson.toJson(square).toString()) ;
						otherParams.put("qrst", square.getClass().getName());
						String otheruserRole = "";
						if (othUserSquareUsers != null && !othUserSquareUsers.isEmpty()) {
							for (QRSquareUser othUserSquareUser : othUserSquareUsers) {
								if (otheruserRole.equals("") || isRoleGreater(othUserSquareUser.getRole().getName(), otheruserRole)) {
									otheruserRole = othUserSquareUser.getRole().getName();
								}
							}
						}
						
						System.out.println("other role : " + otheruserRole);
						String role = "";
						if (userSquareUsers != null && !userSquareUsers.isEmpty()) {
							for (QRSquareUser userSquareUser : userSquareUsers) {
								if (role.equals("") || isRoleGreater(userSquareUser.getRole().getName(), role)) {
									role = userSquareUser.getRole().getName();
								}
							}
							if (isRoleGreater(role, otheruserRole)) {
								if (!(square instanceof QRUserMenager)) {
									choises += addRoleIfYouCan(choises, "editor", role,otheruserRole);
									choises += addRoleIfYouCan(choises, "reader", role,otheruserRole);
								} else {
									choises += addRoleIfYouCan(choises, "friend", role,otheruserRole);
								}
								choises += addRoleIfYouCan(choises, "request", role,otheruserRole);
								choises += addRoleIfYouCan(choises, "remove", role,otheruserRole);
							}
						}
						
								
						otherParams.put("role", role) ;

					}
				} else {
					error += "choose what?";
				}

			} catch (JSONException | InstantiationException | IllegalAccessException e) {
				error += "there are no enough parameters";
			}

		} else {
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
			if(!otherParams.isEmpty()){
				for(String key : otherParams.keySet()){
					myObj.addProperty(key, otherParams.get(key));
				}
			}
			// convert the JSON to string and send back
			return myObj;
		}

	}

	private boolean isRoleGreater(String thisRole, String otherRole) {
		switch (thisRole) {
		case "owner":
			if (!otherRole.equals("owner")) {
				return true;
			}
			break;
		case "editor":
			if (!otherRole.equals("owner") && !otherRole.equals("editor")) {
				return true;
			}
			break;
		case "reader":
			if (!otherRole.equals("owner") && !otherRole.equals("editor") && !otherRole.equals("reader") && !otherRole.equals("friend") && !otherRole.equals("remove")) {
				return true;
			}
			break;
		case "friend":
			if (!otherRole.equals("owner") && !otherRole.equals("editor") && !otherRole.equals("reader") && !otherRole.equals("friend") && !otherRole.equals("remove")) {
				return true;
			}
			break;
		case "request":
			if (!otherRole.equals("owner") && !otherRole.equals("editor") && !otherRole.equals("reader") && !otherRole.equals("friend") && !otherRole.equals("request") && !otherRole.equals("remove")) {
				return true;
			}
			break;
		default:
			break;
		}

		return false;
	}

	private String addRoleIfYouCan(String roles, String role, String myrole, String otherRole) {
		if (roles.contains(role) || !isRoleGreater(myrole, role) || (!otherRole.equals("") && role.equals(otherRole))) {
			return "";
		} else {
			return role + ",";
		}
	}

}
