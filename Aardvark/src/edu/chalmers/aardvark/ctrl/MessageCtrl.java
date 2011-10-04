package edu.chalmers.aardvark.ctrl;

import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.text.format.Time;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.services.MessageSender;

public class MessageCtrl {
    private static MessageCtrl instance;
    private MessageSender messageSender = new MessageSender();

    private MessageCtrl() {
    }

    public static MessageCtrl getInstance() {
	if (instance == null) {
	    instance = new MessageCtrl();
	}
	return instance;
    }

    public void sendMessage(Chat chat, String message) {
	Intent intent = new Intent();
	intent.putExtra("msg", message);
	intent.putExtra("to", chat.getRecipient().getAardvarkID());

	Time time = new Time(Time.getCurrentTimezone());
	time.setToNow();

	ChatMessage chatMessage;
	chatMessage = new ChatMessage(message, LocalUser.getLocalUser(), false,
		time);

	chat.addMessage(chatMessage);

	messageSender.startActivity(intent);
    }

    public void receiveMessage(Packet packet) {
	Time time = new Time(Time.getCurrentTimezone());
	time.setToNow();

	Chat chat = ChatCtrl.getInstance().getChat(packet.getFrom());

	if (chat == null) {
	    // TODO add new chat, set chat variable
	}
	
	ChatMessage chatMessage;
	chatMessage = new ChatMessage(packet.getProperty("message").toString(),
		chat.getRecipient(), true, time);

	chat.addMessage(chatMessage);
    }

}