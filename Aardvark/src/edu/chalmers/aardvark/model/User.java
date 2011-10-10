package edu.chalmers.aardvark.model;

import android.util.Log;

public class User {
    private String alias;
    private String aardvarkID;

    public User(String alias, String aardvarkID) {
	this.alias = alias;
	this.aardvarkID = aardvarkID;
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public String getAardvarkID() {
	return aardvarkID;
    }

    public String getAlias() {
	return alias;
    }

    public void setAlias(String alias) {
	this.alias = alias;
    }
}
