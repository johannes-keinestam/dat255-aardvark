package edu.chalmers.aardvark.model;

public class LocalUser {
	private static User localUser;
	
	public static void createUser(String alias, String aardvarkID) {
		localUser = new User(alias, aardvarkID);
	}
	
	public static User getLocalUser() {
		return localUser;
	}
	
	public static void removeUser() {
		localUser = null;
	}

}
