package edu.chalmers.aardvark.ctrl;

import java.util.List;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.ActiveChatContainer;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.services.MessageSender;
import edu.chalmers.aardvark.services.Notifier;

public class ChatCtrl {
    private static ChatCtrl instance;
    private ActiveChatContainer chatContainer;

    private ChatCtrl() {
	chatContainer = new ActiveChatContainer();
	Log.i("CLASS", this.toString() + " STARTED");
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
    
    public List<Chat> getChats() {
    	return chatContainer.getChats();	
    }

    public void newChat(User user) {
	Chat chat = new Chat(user);

	chatContainer.addChat(chat);
    }
    
    public List<ChatMessage> getChatMessages(String aardvarkID){
	return getChat(aardvarkID).getMessages();
    }
    
    public void sendMessage(Chat chat, String message) {
	Context context = AardvarkApp.getContext();
	Intent intent = new Intent(context, MessageSender.class);
	intent.putExtra("msg", message);
	intent.putExtra("to", chat.getRecipient().getAardvarkID());

	Time time = new Time(Time.getCurrentTimezone());
	time.setToNow();

	ChatMessage chatMessage;
	chatMessage = new ChatMessage(message, LocalUser.getLocalUser(), false,
		time);

	chat.addMessage(chatMessage);

	context.startService(intent);
    }

    public void receiveMessage(Packet packet) {
	String aardvarkID = packet.getFrom();
	if (UserCtrl.getInstance().isUserBlocked(aardvarkID)) {
	    return;
	}
	
	Chat chat = ChatCtrl.getInstance().getChat(aardvarkID);

	if (chat == null) {
	    String alias = ServerHandlerCtrl.getInstance().getAlias(packet.getFrom());
	    ChatCtrl.getInstance().newChat(new User(alias, packet.getFrom()));
	    chat = ChatCtrl.getInstance().getChat(packet.getFrom());
	}
		
	ChatMessage chatMessage;
	
	Time time = new Time(Time.getCurrentTimezone());
	time.setToNow();
	
	Message message = (Message) packet;
	chatMessage = new ChatMessage(message.getBody(),
		chat.getRecipient(), true, time);

	chat.addMessage(chatMessage);
        AardvarkApp.getContext().startService(new Intent(AardvarkApp.getContext(), Notifier.class));
    }
    
    public void closeChat(Chat chat) {
	// TODO 
    }
    
    public void closeChats() {
	chatContainer.getChats().clear();
    }
}
