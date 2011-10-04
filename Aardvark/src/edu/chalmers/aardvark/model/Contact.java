package edu.chalmers.aardvark.model;

public class Contact extends User {
    private String nickname;

    public Contact(String nickname, String aardvarkID) {
	super(null, aardvarkID);
	this.nickname = nickname;
    }

    public String getNickname() {
	return nickname;
    }

    public void rename(String newNickname) {
	nickname = newNickname;
    }

}
