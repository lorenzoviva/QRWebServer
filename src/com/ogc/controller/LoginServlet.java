package com.ogc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ogc.facades.QRUserFacade;
import com.ogc.model.QRUser;
import com.ogc.utility.GsonHelper;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		QRUserFacade facade = new QRUserFacade();
		QRUser qruser = facade.checkQRUserMenager(username, password);
		response.setContentType("application/json");
		if (qruser != null) {
			Gson gson = GsonHelper.customGson;
			// creates json from country object
			JsonElement userObj = gson.toJsonTree(qruser);
			// create a new JSON object
			JsonObject myObj = new JsonObject();
			// add property as success
			myObj.addProperty("success", "true");
			// add the country object
			myObj.add("user", userObj);
			// convert the JSON to string and send back
			response.getWriter().print(myObj);
		} else {
			JsonObject myObj = new JsonObject();
			myObj.addProperty("success", "false");
			response.getWriter().print(myObj);
		}
			
	}
}
