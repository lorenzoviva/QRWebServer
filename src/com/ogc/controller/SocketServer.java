package com.ogc.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.common.collect.Maps;
import com.ogc.facades.ACLFacade;
import com.ogc.facades.InvalidRoleException;
import com.ogc.facades.QRSquareFacade;
import com.ogc.facades.QRUserFacade;
import com.ogc.model.ACL;
import com.ogc.model.QRChat;
import com.ogc.model.QRMessage;
import com.ogc.model.QRSquareFactory;
import com.ogc.model.QRUser;
import com.ogc.utility.JSONUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

@ServerEndpoint("/chat")
public class SocketServer {

	// set to store all the live sessions
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	// Mapping between session and person name
	// <SESSION_ID,NAME CHAT_TEXT>
	private static final Map<String, String> userchatSessionPair = Collections.synchronizedMap(new HashMap<String, String>());

	private JSONUtils jsonUtils = new JSONUtils();
	// <CHAT_TEXT,SET<SESSIONS>>
	private static final Map<String, Set<Session>> chatSessions = Collections.synchronizedMap(new HashMap<String, Set<Session>>());

	private static final Map<String, Session> loginqueue = Collections.synchronizedMap(new HashMap<String, Session>());

	// Getting query params
	public static Map<String, String> getQueryMap(String query) {
		Map<String, String> map = Maps.newHashMap();
		if (query != null) {
			String[] params = query.split("&");
			for (String param : params) {
				String[] nameval = param.split("=");
				map.put(nameval[0], nameval[1]);
			}
		}
		return map;
	}

	/**
	 * Called when a socket connection opened
	 * */

	// It doesn't work with the name "%". Fix needed

	@OnOpen
	public void onOpen(Session session) {

		System.out.println(session.getId() + " has opened a connection " + session.getQueryString());

		Map<String, String> queryParams = getQueryMap(session.getQueryString());

		String name = "";
		String idchat = "";
		for (String key : queryParams.keySet()) {
			System.out.println("key :" + key + "");
		}
		if (queryParams.containsKey("name") && queryParams.containsKey("chat")) {

			// Getting client name via query param
			name = queryParams.get("name");
			idchat = queryParams.get("chat");
			QRUser user = null;
			QRChat chat = null;

			System.out.println("Il nome del nuovo arrivato e': " + name + " ,la chat ha id :" + idchat);
			try {
				name = URLDecoder.decode(name, "UTF-8");
				idchat = URLDecoder.decode(idchat, "UTF-8");
				System.out.println("Il nome decodificato e': " + name + " ,l'id della chat decodificato :" + idchat);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (!name.equals("anonymous")) {
				QRUserFacade facade = new QRUserFacade();
				user = facade.getUserFromId(Long.parseLong(name));
				name = user.getFirstName() + "&" + user.getLastName();
			}

			QRSquareFacade facade = new QRSquareFacade();
			chat = (QRChat) facade.getQRFromText(idchat);

			if (chat == null) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("text", idchat);
				ACLFacade aclfacade = new ACLFacade();
				ACL acl = new ACL(true, true);
				aclfacade.saveACL(acl);
				parameters.put("acl", acl);
				try {
					if (user != null) {
						chat = (QRChat) facade.createNewQRSquare("QRChat", parameters, user);
					} else {
						chat = (QRChat) facade.createNewQRSquare("QRChat", parameters);
					}
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | NoSuchFieldException | SecurityException | InvalidRoleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("chat messages size: " + chat.getMessages().size());
			}

			// Adding session to session list
			sessions.add(session);
			// Adding session to session chat map
			if (chatSessions.isEmpty()) {
				Set<Session> chatSetOfSession = Collections.synchronizedSet(new HashSet<Session>());
				chatSetOfSession.add(session);
				chatSessions.put(chat.getText(), chatSetOfSession);
				System.out.println("Creating the chat holder for :" + chat.getText());

			} else {
				if (chatSessions.keySet().contains(chat.getText())) {
					Set<Session> chatSetOfSession = chatSessions.get(chat.getText());
					chatSetOfSession.add(session);
					chatSessions.put(chat.getText(), chatSetOfSession);
					System.out.println("Adding user to chat holder :" + chat.getText());

				} else {
					Set<Session> chatSetOfSession = Collections.synchronizedSet(new HashSet<Session>());
					chatSetOfSession.add(session);
					chatSessions.put(chat.getText(), chatSetOfSession);
				}
			}
			System.out.println(chat.toJSONObject().toString());
			try {
				// Sending session id to the client that just connected
				session.getBasicRemote().sendText(jsonUtils.getClientDetailsJson(session.getId(), "Your session details", chat.toJSONObject().toString()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Mapping client name and session id
			if (!name.equals("anonymous")) {
				userchatSessionPair.put(session.getId(), String.valueOf(user.getId()) + " " + String.valueOf(chat.getText()));
			} else {
				userchatSessionPair.put(session.getId(), name + " " + String.valueOf(chat.getText()));
			}
			// Notifying all the clients about new person joined
			sendMessageToAll(session.getId(), name, " joined conversation!", true, false);
		} else if (queryParams.containsKey("loginid")) {
			String loginid = queryParams.get("loginid");
			System.out.println(loginid + "sta provando a connettersi con il cellulare");
			loginqueue.put(loginid, session);

		} else if (queryParams.containsKey("authenticate") && queryParams.containsKey("text")) {
			String text = queryParams.get("text");
			long userid = Long.parseLong(queryParams.get("authenticate"));
			QRUserFacade userfacade = new QRUserFacade();
			QRUser user = userfacade.getUserFromId(userid);
			try {
				if (loginqueue.containsKey(text) && user != null) {
					Session s = loginqueue.get(text);
					s.getBasicRemote().sendText(jsonUtils.getAuthenticationJson(s.getId(), "true", user.getJSON().toString()));
					loginqueue.remove(text);
					System.out.println(text + "si è connesso con il cellulare");
					session.getBasicRemote().sendText("d");
				} else if (loginqueue.containsKey(text)) {
					Session s = loginqueue.get(text);
					s.getBasicRemote().sendText(jsonUtils.getAuthenticationJson(s.getId(), "false", ""));
					loginqueue.remove(text);
					session.getBasicRemote().sendText("d");

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * method called when new message received from any client
	 * 
	 * @param message
	 *            JSON message from client
	 * */
	@OnMessage
	public void onMessage(String message, Session session) {

		System.out.println("Message from " + session.getId() + ": " + message + session.getQueryString());

		String msg = null;

		// Parsing the json and getting message
		try {
			JSONObject jObj = new JSONObject(message);
			msg = jObj.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Saving the message
		QRMessage m = null;
		String name = userchatSessionPair.get(session.getId()).split(" ")[0];
		String idchat = userchatSessionPair.get(session.getId()).substring(name.length() + 1);
		if (!name.equals("anonymous")) {
			QRUserFacade userfacade = new QRUserFacade();
			QRUser user = userfacade.getUserFromId(Long.parseLong(name));
			name = user.getFirstName() + "&" + user.getLastName();
			m = new QRMessage(msg, user);
		} else {
			m = new QRMessage(msg);
		}
		QRSquareFacade squarefacade = new QRSquareFacade();
		QRChat chat = (QRChat) squarefacade.getQRFromText(idchat);
		chat.add(m);
		squarefacade.save(chat);

		// Sending the message to all clients
		sendMessageToAll(session.getId(), name, msg, false, false);
	}

	/**
	 * Method called when a connection is closed
	 * */
	@OnClose
	public void onClose(Session session) {

		System.out.println("Session " + session.getId() + " has ended " + session.getQueryString());
		// String userchatSessionPair.get(session.getId()).split(" ")[0];
		// Getting the client name that exited
		String name = userchatSessionPair.get(session.getId()).split(" ")[0];
		String idchat = userchatSessionPair.get(session.getId()).substring(name.length() + 1);
		if (!name.equals("anonymous")) {
			QRUserFacade facade = new QRUserFacade();
			QRUser user = facade.getUserFromId(Long.parseLong(name));
			name = user.getFirstName() + "&" + user.getLastName();
		}

		// Notifying all the clients about person exit
		sendMessageToAll(session.getId(), name, " left conversation!", false, true);

		// removing the session from sessions lists
		userchatSessionPair.remove(session.getId());
		sessions.remove(session);
		Set<Session> chatsessions = chatSessions.get(idchat);
		chatsessions.remove(session);
		if (!chatsessions.isEmpty()) {
			chatSessions.put(idchat, chatsessions);
		} else {
			chatSessions.remove(idchat);
		}

	}

	/**
	 * Method to send message to all clients
	 * 
	 * @param sessionId
	 * @param message
	 *            message to be sent to clients
	 * @param isNewClient
	 *            flag to identify that message is about new person joined
	 * @param isExit
	 *            flag to identify that a person left the conversation
	 * */
	private void sendMessageToAll(String sessionId, String name, String message, boolean isNewClient, boolean isExit) {
		String username = userchatSessionPair.get(sessionId).split(" ")[0];
		String idchat = userchatSessionPair.get(sessionId).substring(username.length() + 1);
		System.out.println("People online total:" + chatSessions.size() + " " + sessions.size() + " " + chatSessions.get(idchat).size());
		// Looping through all the sessions and sending the message individually
		for (Session s : chatSessions.get(idchat)) {
			String json = null;

			// Checking if the message is about new client joined
			if (isNewClient) {
				json = jsonUtils.getNewClientJson(sessionId, name, message, chatSessions.get(idchat).size());
			} else if (isExit) {
				json = jsonUtils.getClientExitJson(sessionId, name, message, chatSessions.get(idchat).size());
				// Checking if the person left the conversation
			} else {
				// Normal chat conversation message
				json = jsonUtils.getSendAllMessageJson(sessionId, name, message);
			}

			try {
				System.out.println("Sending Message To: " + sessionId + ", " + json);
				if (!s.getId().equals(sessionId) || !isExit) {
					s.getBasicRemote().sendText(json);
				}
			} catch (IOException e) {
				System.out.println("error in sending. " + s.getId() + ", " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}