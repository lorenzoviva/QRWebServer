package com.ogc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;
import com.ogc.action.Action;


@WebServlet("/action")
public class ActionController extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String json = request.getParameter("json");
		JSONObject obj = null;
		String error = "";
		JsonObject jsonresponse = null;
		try {
			obj = new JSONObject(json);
			System.out.println("JSONObject :" + obj);
			if (obj.has("action") && obj.has("parameters")) {
				String actionname = obj.getString("action");
				Action action = (Action) Class.forName("com.ogc.action." + Action.correctActionName(actionname)).newInstance();
				jsonresponse = action.perform(obj.getJSONObject("parameters"));
			} else {
				error = "there is no action to perform";
			}
			
		} catch (JSONException | IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			error = "error :" + e.getMessage();
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,POST");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");
		
		if (!error.equals("") || jsonresponse == null ) {
			if(jsonresponse == null){
				error = "there is no response to this request";
			}
			JsonObject myObj = new JsonObject();
			myObj.addProperty("success", false);
			myObj.addProperty("error", error);
			out.println(myObj.toString());
		}else{
			out.println(jsonresponse.toString());
		}
	
	}
}
