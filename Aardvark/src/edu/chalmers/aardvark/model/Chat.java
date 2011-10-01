package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
	private User recipient;
	private List<ChatMessage> chatMessages;
	
	public Chat(User user) {
		chatMessages = new ArrayList<ChatMessage>();
		this.recipient = user;
	}
	
	public void addMessage(ChatMessage msg) {
		chatMessages.add(msg);
	}
	
	public User getRecipient() {
		return recipient;
	}
}
