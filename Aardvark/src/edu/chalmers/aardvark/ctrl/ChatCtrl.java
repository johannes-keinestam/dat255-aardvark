package edu.chalmers.aardvark.ctrl;

import edu.chalmers.aardvark.model.ActiveChatContainer;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.User;

public class ChatCtrl {
    private static ChatCtrl instance;
    private ActiveChatContainer chatContainer;

    private ChatCtrl() {
    }

    public static ChatCtrl getInstance() {
	if (instance == null) {
	    instance = new ChatCtrl();
	}
	return instance;
    }

    public Chat getChat(String aardvarkID) {
	return chatContainer.findChatByID(aardvarkID);
    }

    public void newChat(User user) {
	Chat chat = new Chat(user);

	chatContainer.addChat(chat);
    }
}
