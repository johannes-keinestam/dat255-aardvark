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

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;

/**
 * Service class for receiving status updates (presence) of users from the
 * server. It does this by registering itself as a roster listener. Class sends
 * StateChanges USER_ONLINE and USER_OFFLINE on ComBus.
 * 
 * This class is a service to make Android keep it alive even when the
 * application might not need it.
 */
public class StatusChecker extends Service implements RosterListener {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Called when the service is started.
		Log.i("CLASS", this.toString() + " STARTED");//

		XMPPConnection connection = ServerConnection.getConnection();
		
		// Only add status checker if logged in to prevent crashes.
		if (connection.isAuthenticated()) {
			Roster roster = connection.getRoster();

			// Accept all incoming requests for subscribing to local user's status.
			roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
			// Registers as roster listener.
			roster.addRosterListener(this);
		}
		
		return START_STICKY;
	}

	@Override
	public void presenceChanged(Presence presence) {
		// Status changed for a user, i.e. a user logged in or out.
		// Only proceed if local user is logged in.
		if (ServerHandlerCtrl.getInstance().isLoggedIn()) {
			StateChanges statusChange;
			Log.i("STATUS", "PRESENCE CHANGED FOR USER: " + presence.getFrom());

			if (presence.isAvailable()) {
				statusChange = StateChanges.USER_ONLINE;
				Log.i("STATUS", "LOGGED IN: " + presence.getFrom());
			} else {
				statusChange = StateChanges.USER_OFFLINE;
				Log.i("STATUS", "LOGGED OUT: " + presence.getFrom());
			}

			// Remove suffix added by server on AardvarkID
			String from = presence.getFrom();
			String aardvarkID = from.substring(0, from.lastIndexOf("@"));

			// Send StateChange to listeners (GUI)
			ComBus.notifyListeners(statusChange.toString(), aardvarkID);
		}
	}

	@Override
	public void entriesAdded(Collection<String> addresses) {
		// User created on server, perceive as user logged in (since creating an
		// account is part of our login procedure).
		// Only proceed if local user is logged in.
		if (ServerHandlerCtrl.getInstance().isLoggedIn()) {
			for (String address : addresses) {
				Log.i("STATUS", "USER ADDED, SETTING ONLINE: " + address);
				// Remove suffix added by server on AardvarkID
				String aardvarkID = address.substring(0, address.lastIndexOf("@"));

				// Subscribe to user
				Roster roster = ServerConnection.getConnection().getRoster();
				String alias = ServerHandlerCtrl.getInstance().getAlias(aardvarkID);
				String[] groups = {"Aardvark"};
				try {
					roster.createEntry(address,alias,groups);
				} catch (XMPPException e) {	}
				
				ComBus.notifyListeners(StateChanges.USER_ONLINE.toString(), aardvarkID);
			}
		}

	}

	@Override
	public void entriesDeleted(Collection<String> addresses) {
		// User deleted from server, perceive as user logged out (since deleting
		// the account is part of our logout procedure).
		// Only proceed if local user is logged in.
		if (ServerHandlerCtrl.getInstance().isLoggedIn()) {
			for (String address : addresses) {
				Log.i("STATUS", "USER DELETED, SETTING OFFLINE: " + address);
				
				// Remove suffix added by server on AardvarkID
				String aardvarkID = address.substring(0, address.lastIndexOf("@"));
				ComBus.notifyListeners(StateChanges.USER_OFFLINE.toString(), aardvarkID);
			}
		}

	}

	@Override
	public void entriesUpdated(Collection<String> addresses) {
		// User "updated", could be another device logged into same account or
		// information updated. Ignored.
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Service not bound.
		return null;
	}
}
