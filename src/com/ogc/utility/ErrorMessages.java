package com.ogc.utility;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessages {
	private Map<Integer, String> errorMessages;
	private static ErrorMessages instance = null;
	
	public ErrorMessages(){
		errorMessages = new HashMap<Integer, String>();
		errorMessages.put(-1, "error while creating the new account, please try again later.");
		
	}
	
	
}
