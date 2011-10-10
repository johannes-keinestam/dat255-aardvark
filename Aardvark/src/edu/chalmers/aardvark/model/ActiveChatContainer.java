package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class ActiveChatContainer {

    private List<Chat> activeChats;

    public ActiveChatContainer() {
	activeChats = new ArrayList<Chat>();
	Log.i("INFO", this.toString() + " STARTED");
    }

    public void addChat(Chat chat) {
	activeChats.add(chat);
	ComBus.notifyListeners(StateChanges.CHAT_OPENED.toString(), chat);
    }

    public void removeChat(Chat chat) {
	activeChats.remove(chat);
	ComBus.notifyListeners(StateChanges.CHAT_CLOSED.toString(), chat);
    }

    public Chat findChatByID(String aardvarkID) {
    	Log.i("INFO", "chatcont");
	for (Chat c : activeChats) {
		Log.i("INFO", "chatcont if"+c.getRecipient().getAardvarkID().equals(aardvarkID));
	    if (c.getRecipient().getAardvarkID().equals(aardvarkID)) {
	    	Log.i("INFO", "chatcont if");
		return c;
	    }
	}
	return null;
    }
    public List<Chat> getChats(){
    	return activeChats;
    }

}
