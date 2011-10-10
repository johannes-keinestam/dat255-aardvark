package edu.chalmers.aardvark.services;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ServerConnection;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MessageSender extends IntentService {

    public MessageSender() {
	super("MessageSender");
	Log.i("CLASS", this.toString() + " STARTED");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.i("MESSAGE", "Sending message...");
		String message = intent.getStringExtra("msg");
		String recipient = intent.getStringExtra("to");
	
		if (message != null && recipient != null) {
			String userSuffix = "@"+ServerConnection.getConnection().getServiceName();
			Message messagePacket = new Message(recipient+userSuffix);
			messagePacket.setFrom(LocalUser.getLocalUser().getAardvarkID()+userSuffix);
			messagePacket.setType(Message.Type.chat);
			messagePacket.setBody(message);
	
	    	Log.i("MESSAGE", "Sending packet: "+messagePacket.toXML());

		    XMPPConnection connection = ServerConnection.getConnection();
		    connection.sendPacket(messagePacket);
		}

    }

}
