package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

public class ActiveChatContainer {

	private List<Chat> activeChats;
	
	public ActiveChatContainer() {
		activeChats = new ArrayList<Chat>();
	}
	
	public void addChat(Chat chat) {
		activeChats.add(chat);
	}
	
	public void removeChat(Chat chat) {
		activeChats.remove(chat);
	}
	
	public Chat findChatByID(String aardvarkID) {
		for (Chat c : activeChats) {
			if (c.getRecipient().getAardvarkID().equals(aardvarkID)) {
				return c;
			}
		}
		return null;
	}
	
}
