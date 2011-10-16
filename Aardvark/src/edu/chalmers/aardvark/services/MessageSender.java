/**
 * Copyright 2011 Fredrik Hidstrand, Johannes Keinestam, Magnus Sjöqvist, Fredrik Thander
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

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ServerConnection;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * IntentService class (automatically threaded service) for sending chat message
 * packets to the server. Starting the service should be done with an intent
 * with the string extras named "msg" and "to" containing the message body and
 * the AardvarkID (without the @server-name suffix), respectively.
 * 
 * This class is a service to make Android keep it alive even when the
 * application might not need it.
 */
public class MessageSender extends IntentService {

	/**
	 * Constructor, initializing the class. Not to be called explicitly.
	 */
	public MessageSender() {
		super("MessageSender");
		Log.i("CLASS", this.toString() + " STARTED");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Method called in worker thread when intent received.
		Log.i("MESSAGE", "Sending message...");

		// Gets message details from intent.
		String message = intent.getStringExtra("msg");
		String recipient = intent.getStringExtra("to");

		// Only send message packet if it has complete information.
		if (message != null && recipient != null) {
			String userSuffix = "@" + ServerConnection.getConnection().getServiceName();
			Message messagePacket = new Message(recipient + userSuffix);
			messagePacket.setFrom(LocalUser.getLocalUser().getAardvarkID() + userSuffix);
			messagePacket.setType(Message.Type.chat);
			messagePacket.setBody(message);

			Log.i("MESSAGE", "Sending packet: " + messagePacket.toXML());

			XMPPConnection connection = ServerConnection.getConnection();
			connection.sendPacket(messagePacket);
		}
	}
}
