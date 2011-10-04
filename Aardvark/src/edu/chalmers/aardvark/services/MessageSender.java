package edu.chalmers.aardvark.services;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;

import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.MessagePacket;
import edu.chalmers.aardvark.util.ServerConnection;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MessageSender extends IntentService {

    public MessageSender() {
	super("MessageSender");
	Log.i("INFO", this.toString() + " STARTED");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
	String message = intent.getStringExtra("msg");
	String recipient = intent.getStringExtra("to");

	if (message != null && recipient != null) {
	    Packet messagePacket = new MessagePacket(LocalUser.getLocalUser()
		    .getAardvarkID(), recipient, message);

	    XMPPConnection connection = ServerConnection.getConnection();
	    connection.sendPacket(messagePacket);
	}

    }

}
