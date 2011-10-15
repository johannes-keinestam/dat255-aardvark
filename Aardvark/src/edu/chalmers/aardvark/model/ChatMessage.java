package edu.chalmers.aardvark.model;

import android.text.format.Time;
import android.util.Log;

public class ChatMessage {
	private String message;
	private User user;
	private boolean isRemote;
	private Time timeStamp;

	public ChatMessage(String msg, User user, boolean remote, Time time) {
		message = msg;
		this.user = user;
		isRemote = remote;
		timeStamp = time;
		Log.i("CLASS", this.toString() + " STARTED");
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
