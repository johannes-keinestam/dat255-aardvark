package edu.chalmers.aardvark.services;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;
import org.jivesoftware.smack.packet.Packet;

import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.MessagePacket;
import edu.chalmers.aardvark.util.ServerConnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MessageReceiver extends Service {

	@Override
	public void onCreate() {
		PacketFilter filter = new ToContainsFilter(LocalUser.getLocalUser().getAardvarkID());
		PacketListener incomingMsgListener = new PacketListener() {
			public void processPacket(Packet packet) {
				if (packet instanceof MessagePacket) {
					//MessageCtrl.receiveMessage(packet);
				}
			}
		};
		
		ServerConnection.getConnection().addPacketListener(incomingMsgListener, filter);
		
		//TODO start thread
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//TODO send message in thread
		//if OS kills service, it will start up again
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		//Not used
		return null;
	}
	

}
