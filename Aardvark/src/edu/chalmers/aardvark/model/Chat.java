package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class Chat {
    private User recipient;
    private List<ChatMessage> chatMessages;

    public Chat(User user) {
	chatMessages = new ArrayList<ChatMessage>();
	recipient = user;
    }

    public void addMessage(ChatMessage msg) {
	chatMessages.add(msg);
	ComBus.notifyListeners(StateChanges.NEW_MESSAGE_IN_CHAT.toString(), this);
    }

    public User getRecipient() {
	return recipient;
    }
}
