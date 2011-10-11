package edu.chalmers.aardvark.model;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class Chat {
    private User recipient;
    private List<ChatMessage> chatMessages;
    private boolean encryptionEnabled;
    private RSAPublicKey recipientPublicKey;

    public Chat(User user) {
	chatMessages = new ArrayList<ChatMessage>();
	recipient = user;
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public void addMessage(ChatMessage msg) {
	chatMessages.add(msg);
	ComBus.notifyListeners(StateChanges.NEW_MESSAGE_IN_CHAT.toString(), this);
    }

    public User getRecipient() {
	return recipient;
    }
    
    public List<ChatMessage> getMessages(){
	return chatMessages;
    }
    
    public void setEncryption(boolean enabled){
	encryptionEnabled = enabled;
    }    
    
    public boolean isEncrypted(){
	return encryptionEnabled;
    }
    
    public void setRecipientPublicKey(RSAPublicKey recipientPublicKey){
	this.recipientPublicKey = recipientPublicKey;
    }
    
    public RSAPublicKey getRecipientPublicKey(){
	return recipientPublicKey;
    }
}
