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

package edu.chalmers.aardvark.ctrl;

import java.util.Map;
import java.util.UUID;

import android.content.SharedPreferences;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ServerConnection;

/**
 * Controller class which handles startup and shutdown procedures.
 */
public class SystemCtrl {
	/** Static variable for singleton instance */
	private static SystemCtrl instance;

	/**
	 * Private constructor.
	 */
	private SystemCtrl() {
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of SystemCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static SystemCtrl getCtrl() {
		if (instance == null) {
			instance = new SystemCtrl();
		}
		return instance;
	}

	/**
	 * Procedures performed at start up. Includes loading saved data (local
	 * user, contacts, block list) and starting server connection.
	 */
	public void performStartUpDuty() {
		// If it is first start up, run setup.
		if (isFirstRun()) {
			performSetup();
		}

		// Start instances of controller classes.
		ChatCtrl.getInstance();
		ServerHandlerCtrl.getInstance();
		ContactCtrl contactCtrl = ContactCtrl.getInstance();
		UserCtrl userCtrl = UserCtrl.getInstance();

		// Loads contacts from file
		SharedPreferences savedContacts = AardvarkApp.getContext().getSharedPreferences("contacts",
				0);
		for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
			contactCtrl.addContact((String) entry.getValue(), entry.getKey());
		}

		// Loads list of blocked users from file
		SharedPreferences blockedUsers = AardvarkApp.getContext().getSharedPreferences("blocklist",
				0);
		for (Map.Entry<String, ?> entry : blockedUsers.getAll().entrySet()) {
			userCtrl.blockUser((String) entry.getKey());
		}

		// Loads local user unique identifiers/server details from file
		SharedPreferences savedLocalUser = AardvarkApp.getContext().getSharedPreferences(
				"localuser", 0);
		String aardvarkID = savedLocalUser.getString("ID", null);
		String password = savedLocalUser.getString("password", null);
		LocalUser.createUser(aardvarkID, password);

		// Set up and create new connection to server.
		ServerConnection.getConnection();
	}

	/**
	 * Procedures performed at shutdown.
	 */
	public void performShutDownDuty() {
		// Nothing to do at the moment

		// Kill connection to server
		ServerConnection.restart();
	}

	/**
	 * Procedures only performed at first start up, setting up the program.
	 * Includes created a unique AardvarkID used as a username on the server,
	 * and a password for the user.
	 */
	private void performSetup() {
		// generate unique hashed ID for user identification (Aardvark
		// ID)
		String aardvarkID = UUID.randomUUID().toString();

		// generate pseudo-random password
		String genPassword = UUID.randomUUID().toString();

		// save AardvarkID and password as persistent data
		SharedPreferences savedLocalUser = AardvarkApp.getContext().getSharedPreferences(
				"localuser", 0);
		SharedPreferences.Editor editor = savedLocalUser.edit();

		editor.putString("ID", aardvarkID);
		editor.putString("password", genPassword);
		editor.commit();

		Log.i("Id PASS", aardvarkID + "::" + genPassword);

		// In future, generate encryption keys, save

		// indicate that the application is now setup for future use
		SharedPreferences settings = AardvarkApp.getContext().getSharedPreferences("settings", 0);
		SharedPreferences.Editor settingsEditor = settings.edit();

		settingsEditor.putBoolean("firstRun", false);
		settingsEditor.commit();

	}

	/**
	 * Checks if it is the first time Aardvark is run on the device.
	 * 
	 * @return true if first run, false if not.
	 */
	private boolean isFirstRun() {
		// Check firstRun flag
		SharedPreferences settings = AardvarkApp.getContext().getSharedPreferences("settings", 0);

		return settings.getBoolean("firstRun", true);
	}
}
