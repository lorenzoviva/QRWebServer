package com.ogc.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.facades.QRSquareUserFacade;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.utility.GsonHelper;

public class Users<e> extends Action {

	private static Class[] subactionarray = { Add.class, Change.class, Remove.class, Back.class };

	private static int maxusers = 3;

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
			// initializing variables
			String text = "";// = parameters.getString("text");
			long userid = -1;// = parameters.getLong("user");
			long othuserid = -1;
			QRSquare reqSquare = null;
			QRUser reqUser = null;
			int request = parameters.getInt("request");// 1 : only
														// QRSquare.users 2 :
														// QRSquare.users+User.QRUserMenager.users
														// 3: only
														// QRUser.squares
			int listindex = parameters.getInt("listindex");
			QRUser othUser = null;
			Gson gson = GsonHelper.customGson;

			// Preparing for fetch and correcting request value
			QRSquareUserFacade qrSquareUserfacade = new QRSquareUserFacade();
			if (parameters.has("QRUser")) {
				userid = parameters.getLong("QRUser");
				if (parameters.has("user")) {
					othuserid = parameters.getLong("user");
				}
			} else {
				if (parameters.has("user")) {
					userid = parameters.getLong("user");
				}
			}
			if (parameters.has("text")) {
				text = parameters.getString("text");
			}

			QRUserMenager menager = null;
			if (userid != -1) {
				menager = qrSquareUserfacade.getQRUserMenager(userid);
				reqUser = qrSquareUserfacade.getQRUser(userid);
			}
			if (!text.equals("")) {
				reqSquare = qrSquareUserfacade.getQRSquare(text);
			}
			if (othuserid != -1) {
				othUser = qrSquareUserfacade.getQRUser(othuserid);
			}
			if (userid == -1 && request == 3) {
				request = 1;
			}
			if (!text.equals("") && userid != -1) {

				if (request == 2 && menager.getText().equals(text)) {
					request = 1;
				} else if (request == 1 && !menager.getText().equals(text)) {
					request = 2;
				}
			} else if ((text.equals("") && userid == -1) || request > 3 || request < 1 || (userid == -1 && request == 3) || (text.equals("") && (request == 1 || request == 2))) {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;
			}

			// Fetching and doing operations on fetched data (if request = 2 we
			// have so sum QRSquareUsers of both QRUsers and QRSquares
			// interested );
			List<QRSquareUser> squareUsers = null;
			List<QRSquareUser> myfriendsSquareUsers = null;
			List<QRUserMenager> squareUsersMenagers = null;
			List<ACL> aclList = null;
			List<String> squareUsersSquareTypes = null;
			List<QRSquareUser> squareUsersList = null;
			List<QRSquareUser> reverseSquareUsersList = null;
			if (request == 1 || request == 2) {
				squareUsers = qrSquareUserfacade.getSquareUsers(text);
				if (request == 2 && userid != -1) {
					squareUsersList = new ArrayList<QRSquareUser>(squareUsers);
					myfriendsSquareUsers = qrSquareUserfacade.getSquareUsers(menager.getText());
					for (QRSquareUser qrsu : myfriendsSquareUsers) {
						QRUser cuser = qrsu.getUser();
						boolean insert = true;
						for (int i = 0; i < squareUsersList.size(); i++) {
							if (cuser.getId() == squareUsersList.get(i).getUser().getId()) {
								insert = false;
							}
						}
						if (insert) {
							qrsu.setIsnew(true);
							squareUsersList.add(qrsu);
						}
					}
					squareUsersMenagers = qrSquareUserfacade.getSquareUsersMenager(squareUsersList);
				} else {
					squareUsersMenagers = qrSquareUserfacade.getSquareUsersMenager(squareUsers);
				}
				Map<String, QRSquareUser> mySquareUsers = null;
				aclList = new ArrayList<ACL>();
				JsonObject aclparameters = new JsonObject();
				if (userid != -1) {
					aclparameters.addProperty("user", reqUser.getId());
					mySquareUsers = qrSquareUserfacade.getQRMenagerUsers(squareUsersMenagers, reqUser.getId());

				}
				aclparameters.addProperty("users", "x");

				for (int i = 0; i < squareUsersMenagers.size(); i++) {
					if (i != 0) {
						aclparameters.remove("QRSquare");
						aclparameters.remove("type");
						aclparameters.remove("QRSquareUser");
					}

					aclparameters.add("QRSquare", gson.toJsonTree(squareUsersMenagers.get(i)));
					aclparameters.addProperty("type", QRUserMenager.class.getName());

					List<QRSquareUser> qrSquareUser = new ArrayList<QRSquareUser>();
					if (userid != -1 && mySquareUsers.containsKey(squareUsersMenagers.get(i).getText())) {
						if (!mySquareUsers.get(squareUsersMenagers.get(i).getText()).getIsnew()) {
							qrSquareUser.add(mySquareUsers.get(squareUsersMenagers.get(i).getText()));
						}
					}
					if (!qrSquareUser.isEmpty() && !qrSquareUser.get(0).getIsnew()) {
						aclparameters.add("QRSquareUser", gson.toJsonTree(qrSquareUser));
					}

					boolean read = (new Read()).canPerform(aclparameters);
					boolean write = (new Edit()).canPerform(aclparameters);
					ACL acl = new ACL(read, write);
					aclList.add(acl);
				}
			} else {
				squareUsers = qrSquareUserfacade.getUserSquares(userid);
				List<QRSquare> squares = qrSquareUserfacade.getSquareUsersSquares(squareUsers);
				squareUsersSquareTypes = new ArrayList<String>();

				for (QRSquare square : squares) {
					squareUsersSquareTypes.add(square.getClass().getSimpleName());
				}

				Map<String, QRSquareUser> mySquareUsers = null;
				aclList = new ArrayList<ACL>();
				JsonObject aclparameters = new JsonObject();
				if (othuserid != -1) {
					mySquareUsers = qrSquareUserfacade.getQRSquareUsers(squares, othuserid);
					aclparameters.addProperty("user", othUser.getId());
				}
				aclparameters.addProperty("users", "x");

				for (int i = 0; i < squares.size(); i++) {
					if (i != 0) {
						aclparameters.remove("QRSquare");
						aclparameters.remove("type");
						aclparameters.remove("QRSquareUser");
					}
					aclparameters.add("QRSquare", gson.toJsonTree(squares.get(i)));
					aclparameters.addProperty("type", squares.get(i).getClass().getName());
					List<QRSquareUser> qrSquareUser = new ArrayList<QRSquareUser>();
					if (othuserid != -1 && mySquareUsers.containsKey(squares.get(i).getText())) {
						if (!mySquareUsers.get(squares.get(i).getText()).getIsnew()) {
							qrSquareUser.add(mySquareUsers.get(squares.get(i).getText()));
						}
					}
					if (!qrSquareUser.isEmpty() && !qrSquareUser.get(0).getIsnew()) {
						aclparameters.add("QRSquareUser", gson.toJsonTree(qrSquareUser));
						System.out.println("Added QRSquareUsers with:" + gson.toJsonTree(qrSquareUser));
					}
					boolean read = (new Read()).canPerform(aclparameters);
					boolean write = (new Edit()).canPerform(aclparameters);
					ACL acl = new ACL(read, write);
					aclList.add(acl);
				}

			}

			if (squareUsers == null || squareUsers.isEmpty()) {
				JsonObject myObj = new JsonObject();
				myObj.addProperty("success", false);
				return myObj;

			} else {

				// create a new JSON object
				JsonObject myObj = new JsonObject();
				// add property as success
				myObj.addProperty("success", true);
				myObj.addProperty("request",request);
				// add the objects
				ACL firstacl = null;
				QRSquareUser firstSquareUser = null;
				if (aclList != null || !aclList.isEmpty()) {
					firstacl = aclList.get(0);
					if (aclList.size() > maxusers) {
						aclList = cutList(aclList, listindex);
					}
					JsonElement aclListObj = gson.toJsonTree(aclList);
					myObj.add("ACLList", aclListObj);
				}
				if (squareUsersList == null) {
					firstSquareUser = squareUsers.get(0);
					if (squareUsers.size() > maxusers) {
						if (listindex + maxusers < squareUsers.size()) {
							myObj.addProperty("listindex", listindex + maxusers - 1);
						} else {
							myObj.addProperty("listindex", squareUsers.size() - 1);
						}
						myObj.addProperty("totusers", squareUsers.size());
						myObj.addProperty("maxusers", maxusers);
						squareUsers = cutList(squareUsers, listindex);

					} else {
						myObj.addProperty("maxusers", maxusers);
						myObj.addProperty("listindex", squareUsers.size() - 1);
						myObj.addProperty("totusers", squareUsers.size());
					}
					JsonElement squareUsersObj = gson.toJsonTree(squareUsers);
					myObj.add("QRSquareUser", squareUsersObj);
				} else {// request = 2
					if (squareUsersList.size() > maxusers) {
						if (listindex + maxusers < squareUsersList.size()) {
							myObj.addProperty("listindex", listindex + maxusers - 1);
						} else {
							myObj.addProperty("listindex", squareUsersList.size() - 1);
						}
						myObj.addProperty("totusers", squareUsersList.size());
						myObj.addProperty("maxusers", maxusers);
						squareUsersList = cutList(squareUsersList, listindex);

					} else {
						myObj.addProperty("maxusers", maxusers);
						myObj.addProperty("listindex", squareUsersList.size() - 1);
						myObj.addProperty("totusers", squareUsersList.size());
					}
					JsonElement squareUsersListObj = gson.toJsonTree(squareUsersList);
					myObj.add("QRSquareUser", squareUsersListObj);
				}
			
				if (request == 1) {// request = 1
					JsonElement squareObj = gson.toJsonTree(firstSquareUser.getSquare());
					// JsonElement userObj =
					// gson.toJsonTree(squareUsers.get(0).getUser());
					myObj.add("QRSquare", squareObj);
					myObj.addProperty("type", firstSquareUser.getSquare().getClass().getSimpleName());
					myObj.add("acl", gson.toJsonTree(new ACL(true,true)));
					// myObj.add("QRUser", userObj);
				} else {// request = 2|3
					if (!text.equals("")) {// request = 2
						JsonElement squareObj = gson.toJsonTree(reqSquare);
						myObj.add("QRSquare", squareObj);
						myObj.addProperty("type", reqSquare.getClass().getSimpleName());
						myObj.add("acl", gson.toJsonTree(firstacl));
					} else {// request = 3
						JsonElement squareObj = gson.toJsonTree(squareUsers.get(0).getSquare());
						myObj.add("QRSquare", squareObj);
						myObj.addProperty("type", squareUsers.get(0).getSquare().getClass().getSimpleName());
						myObj.add("acl", gson.toJsonTree(aclList.get(0)));
					}

				}
				if (userid != -1) {// request = 1|2|3
					if (othuserid != -1) {// request = 3
						JsonElement othuserObj = gson.toJsonTree(othUser);
						myObj.add("user", othuserObj);
					}
					JsonElement userObj = gson.toJsonTree(reqUser);
					myObj.add("QRUser", userObj);

				}
				if (squareUsersMenagers != null) {
					if (squareUsersMenagers.size() > maxusers) {
						squareUsersMenagers = cutList(squareUsersMenagers, listindex);
					}
					JsonElement squareUsersMenagersObj = gson.toJsonTree(squareUsersMenagers);
					myObj.add("QRUserMenagers", squareUsersMenagersObj);
				}
				if (squareUsersSquareTypes != null) {
					if (squareUsersSquareTypes.size() > maxusers) {
						squareUsersSquareTypes = cutList(squareUsersSquareTypes, listindex);
					}
					JsonElement squareUsersSquareTypesObj = gson.toJsonTree(squareUsersSquareTypes);
					myObj.add("QRSquares", squareUsersSquareTypesObj);
				}
				myObj.addProperty("requestmessage", parameters.toString());
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

	private <e> List<e> cutList(List<e> otherlist, int listindex) {
		List<e> list = new ArrayList<e>();
		if (listindex + maxusers < otherlist.size()) {
			for (int i = listindex; i < listindex + maxusers; i++) {
				list.add(otherlist.get(i));
			}
		} else {
			for (int i = listindex; i < otherlist.size(); i++) {
				list.add(otherlist.get(i));
			}
		}

		return list;
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
