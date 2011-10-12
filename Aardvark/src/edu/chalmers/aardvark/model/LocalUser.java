package edu.chalmers.aardvark.model;

import android.util.Log;

public class LocalUser {
    private static User localUser;
    private static String password;

    public static void createUser(String aardvarkID, String pass) {
	localUser = new User(null, aardvarkID);
	password = pass;
	Log.i("CLASS", "Localuser started!");
    }

    public static void setAlias(String alias) {
	localUser.setAlias(alias);
    }

    public static User getLocalUser() {
	return localUser;
    }

    public static String getPassword() {
	return password;
    }

    public static void removeUser() {
	localUser = null;
    }

}
