package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class Chat {
	private User recipient;
	private List<ChatMessage> chatMessages;
	private int unreadMessages = 0;

	public Chat(User user) {
		chatMessages = new ArrayList<ChatMessage>();
		recipient = user;
		Log.i("CLASS", this.toString() + " STARTED");
	}

	public void addMessage(ChatMessage msg) {
		chatMessages.add(msg);
		unreadMessages++;
		ComBus.notifyListeners(StateChanges.NEW_MESSAGE_IN_CHAT.toString(), this);
	}

	public User getRecipient() {
		return recipient;
	}

	public List<ChatMessage> getMessages() {
		return chatMessages;
	}

	public void clearUnreadMessages() {
		unreadMessages = 0;
	}

	public int unreadMessages() {
		return unreadMessages;
	}
}
