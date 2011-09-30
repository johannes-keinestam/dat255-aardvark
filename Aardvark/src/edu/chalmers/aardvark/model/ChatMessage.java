package edu.chalmers.aardvark.model;

import android.text.format.Time;

public class ChatMessage {
	private String message;
	private User user;
	private boolean isRemote;
	private Time timeStamp;
	
	public ChatMessage(String msg, User user, boolean remote, Time time) {
		this.message = msg;
		this.user = user;
		this.isRemote = remote;
		this.timeStamp = time;
	}
	
	public String getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}
	
	public boolean isRemote() {
		return isRemote;
	}
	
	public Time getTimeStamp() {
		return timeStamp;
	}
}
