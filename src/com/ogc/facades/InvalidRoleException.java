package com.ogc.facades;

public class InvalidRoleException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public void printStackTrace(){
		System.out.println("ERORR : " + this.getClass().getSimpleName());
	}
}
