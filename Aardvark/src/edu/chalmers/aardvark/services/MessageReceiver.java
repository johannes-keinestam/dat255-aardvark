/**
 * Copyright 2011 Fredrik Hidstrand, Johannes Keinestam, Magnus Sj�qvist, Fredrik Thander
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.chalmers.aardvark.services;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ServerConnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Service class for receiving chat message packets from the server. It does
 * this by registering itself as a packet listener. When valid chat messages are
 * received, they are sent to the ChatCtrl.
 * 
 * This class is a service to make Android keep it alive even when the
 * application might not need it.
 */
public class MessageReceiver extends Service implements PacketListener {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(ServerConnection.getConnection() != null && ServerConnection.getConnection().isConnected()){
			// Method called when an intent was received through startService
			PacketFilter filter = new ToContainsFilter(LocalUser.getLocalUser().getAardvarkID());
	
			// Register as packet listener.
			ServerConnection.getConnection().addPacketListener(this, filter);
			Log.i("CLASS", this.toString() + " STARTED");

		}
		// If killed, restart when new intent received.
		return START_STICKY;
	}

	@Override
	public void processPacket(Packet packet) {
		// Method called when a packet was received from server.
		Log.i("MESSAGE", "Received packet, processing...");
		// If packet was a chat message, receive it. If not, discard.
		if (packet instanceof Message) {
			Log.i("MESSAGE", "Message received from " + packet.getFrom());

			// Cut of suffix appended by server
			String fromUser = packet.getFrom();
			String fromAardvarkID = fromUser.substring(0, fromUser.lastIndexOf("@"));
			packet.setFrom(fromAardvarkID);

			ChatCtrl.getInstance().receiveMessage(packet);
		} else {
			Log.i("MESSAGE", "Unknown package received from " + packet.getFrom());
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Service not bound.
		return null;
	}

}
