package edu.chalmers.aardvark.services;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;
import org.jivesoftware.smack.packet.Packet;

import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.MessagePacket;
import edu.chalmers.aardvark.util.ServerConnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MessageReceiver extends Service implements PacketListener {

    @Override
    public void onCreate() {
	PacketFilter filter = new ToContainsFilter(LocalUser.getLocalUser()
		.getAardvarkID());

	ServerConnection.getConnection().addPacketListener(this, filter);
	Log.i("INFO", this.toString() + " STARTED");
    }

    @Override
    public void processPacket(Packet packet) {
	if (packet instanceof MessagePacket) {
	    ChatCtrl.getInstance().receiveMessage(packet);
	}
    }

    @Override
    public IBinder onBind(Intent arg0) {
	// Not used
	return null;
    }
    
    @Override
    public void onDestroy() {
	ServerHandlerCtrl.getInstance().logOut();
    }

}
