package com.ogc.action;

import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;
import com.ogc.model.QRSquare;
import com.ogc.utility.GsonHelper;

public class Link extends Action {

	public Link() {
		super(null);
	}

	@Override
	public boolean canPerform(JsonObject parameters) {
		if (!parameters.has("text") && !parameters.has("QRSquare")) {
			return false;
		} else {
			if (parameters.has("text")) {
				boolean link = true;
				try {
					new URL(parameters.get("text").getAsString());
				} catch (MalformedURLException e) {
					link = false;
				}
				return link;
			} else {
				QRSquare square = (GsonHelper.customGson).fromJson(parameters.get("QRSquare"),QRSquare.class);
				boolean link = true;
				try {
					new URL(square.getText());
				} catch (MalformedURLException e) {
					link = false;
				}
				return link;
			}

		}
	}

	@Override
	public JsonObject perform(JSONObject parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
