package com.ogc.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.codehaus.jettison.json.JSONObject;

import com.google.gson.JsonObject;

public abstract class Action {

	private List<Class<? extends Action>> subactions;

	
	public Action(Class<? extends Action> subactions[]) {
		if (subactions != null) {
			for (int i = 0; i < subactions.length; i++) {
//				System.out.println("adding action :" + subactions[i]);
				addSubaction(subactions[i]);
			}
		}
	}

	public abstract boolean canPerform(JsonObject parameters);

	public abstract JsonObject perform(JSONObject parameters);

	public static String correctActionName(String string) {
		return string.substring(0, 1).toUpperCase(Locale.ROOT).concat(string.substring(1, string.length()));
	}

	protected String getPossibleActions(JsonObject parameters) {

		String possibleActions = "";
		for (int i = 0; i < subactions.size(); i++) {
			String actionName = subactions.get(i).getName();
//			System.out.println("testing action :" + actionName);
			if (!actionName.endsWith("Action")) {
				try {
					Action action = (Action) Class.forName(actionName).newInstance();
					if (action.canPerform(parameters)) {
						possibleActions += subactions.get(i).getSimpleName().toLowerCase() + ",";
						System.out.println("ACTION:" + subactions.get(i).getSimpleName());
					}
				} catch (InstantiationException e) {
					System.out.println("ERROR:" + subactions.get(i).getSimpleName());
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return possibleActions;

	}

	public List<Class<? extends Action>> getSubactions() {
		return subactions;
	}

	public void setSubactions(List<Class<? extends Action>> subactions) {
		this.subactions = subactions;
	}

	public void addSubaction(Class<? extends Action> action) {
		if (subactions == null || subactions.isEmpty()) {
			subactions = new ArrayList<Class<? extends Action>>();
		}
		subactions.add(action);

	}
}
