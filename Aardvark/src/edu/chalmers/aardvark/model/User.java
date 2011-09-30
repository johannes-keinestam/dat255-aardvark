package edu.chalmers.aardvark.model;

public class User {
	private String alias;
	private String aardvarkID;
	
	public User(String alias, String aardvarkID) {
		this.alias = alias;
		this.aardvarkID = aardvarkID;
	}
	
	public String getAardvarkID() {
		return aardvarkID;
	}
	
	public String getAlias() {
		return alias;
	}
}
