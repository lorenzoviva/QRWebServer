package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.openejb.resource.quartz.QuartzResourceAdapter;
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
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.utility.GsonHelper;

public class Users extends Action {

	private static Class[] subactionarray = { Add.class, Change.class, Remove.class, Back.class };

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
			String text = "";// = parameters.getString("text");
			long userid = -1;// = parameters.getLong("user");
			int request = parameters.getInt("request");//1 : only QRSquare.users	2 : QRSquare.users+User.QRUserMenager.users		3: only QRUser.squares 
			QRSquareUserFacade qrSquareUserfacade = new QRSquareUserFacade();
			if (parameters.has("user")) {
				userid = parameters.getLong("user");
			}
			if (parameters.has("text")) {
				text = parameters.getString("text");
			}
			QRUserMenager menager = null;
			if(userid != -1){
				menager = qrSquareUserfacade.getQRUserMenager(userid);
			}
			if(userid == -1 && request == 3){
				request = 1;
			}
			if (!text.equals("") && userid != -1) {
				
				if(request == 2 && menager.getText().equals(text)){
					request = 1;
				}else if(request == 1 && !menager.getText().equals(text)){
					request = 2;
				}
			}else if((text.equals("") && userid == -1 ) || request>3 || request < 1){
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;
			}
			List<QRSquareUser> squareUsers = null;
			List<QRSquareUser> myfriendsSquareUsers = null;
			List<QRUserMenager> squareUsersMenagers = null;
			List<QRSquare> squareUsersSquares = null;
			List<QRSquareUser> squareUsersList = null;
			if (request == 1 || request == 2){
				squareUsers = qrSquareUserfacade.getSquareUsers(text);
				if(request == 2 && userid != -1){
					squareUsersList = new ArrayList<QRSquareUser>(squareUsers);
					myfriendsSquareUsers = qrSquareUserfacade.getSquareUsers(menager.getText());
					for(QRSquareUser qrsu : myfriendsSquareUsers){
						QRUser cuser = qrsu.getUser(); 
						boolean insert = true;
						for(int i = 0; i < squareUsersList.size() ; i++){
							if(cuser.getId()==squareUsersList.get(i).getUser().getId()){
								insert = false;
							}
						}
						if(insert){
							squareUsersList.add(qrsu);
						}
					}
					squareUsersMenagers = qrSquareUserfacade.getSquareUsersMenager(squareUsersList);
					
				}else{
					squareUsersMenagers = qrSquareUserfacade.getSquareUsersMenager(squareUsers);
				}
			}else{
				squareUsers = qrSquareUserfacade.getUserSquares(userid);
				squareUsersSquares = qrSquareUserfacade.getSquareUsersSquares(squareUsers);
			}
			if (squareUsers == null || squareUsers.isEmpty()) {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;

			} else {
				Gson gson = GsonHelper.customGson;
				// create a new JSON object
				JsonObject myObj = new JsonObject();
				// add property as success
				myObj.addProperty("success", true);
				// add the  objects
				if(squareUsers !=null && squareUsersList==null){
					JsonElement squareUsersObj = gson.toJsonTree(squareUsers);
					myObj.add("QRSquareUser", squareUsersObj);
					JsonElement squareObj = gson.toJsonTree(squareUsers.get(0).getSquare());
					JsonElement userObj = gson.toJsonTree(squareUsers.get(0).getUser());
					myObj.add("QRUser", userObj);
					myObj.add("QRSquare", squareObj);

				}else if(squareUsersList != null){
					JsonElement squareUsersListObj = gson.toJsonTree(squareUsersList);
					myObj.add("QRSquareUser", squareUsersListObj);
					JsonElement squareObj = gson.toJsonTree(squareUsers.get(0).getSquare());
					JsonElement userObj = gson.toJsonTree(squareUsers.get(0).getUser());
					myObj.add("QRUser", userObj);
					myObj.add("QRSquare", squareObj);
				}
				if(squareUsersMenagers !=null){
					JsonElement squareUsersMenagersObj = gson.toJsonTree(squareUsersMenagers);
					myObj.add("QRUserMenagers", squareUsersMenagersObj);
				}
				if(squareUsersSquares != null){
					JsonElement squareUsersSquaresObj = gson.toJsonTree(squareUsersSquares);
					myObj.add("QRSquares", squareUsersSquaresObj);
				}
				myObj.addProperty("request", request);
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
			Gson gson = GsonHelper.customGson;
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
						List<QRSquareUser> squareUser = GsonHelper.customGson.fromJson(parameters.get("QRSquareUser"), listType);
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
