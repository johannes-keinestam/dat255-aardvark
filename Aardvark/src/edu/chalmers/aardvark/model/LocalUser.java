package edu.chalmers.aardvark.model;

public class LocalUser {
	private static User localUser;
	private static String password;
	
	public static void createUser(String alias, String aardvarkID, String pass) {
		localUser = new User(alias, aardvarkID);
		password = pass;
	}
	
	public static User getLocalUser() {
		return localUser;
	}
	
	public static void removeUser() {
		localUser = null;
	}

}
