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

package edu.chalmers.aardvark.util;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.services.MessageReceiver;
import edu.chalmers.aardvark.services.MessageSender;
import edu.chalmers.aardvark.services.StatusChecker;

/**
 * Basically a wrapper class for the XMPPConnection object, which connects to
 * the Aardvark server. Configures server settings, and handles connecting and
 * disconnecting from the server. More specific actions may be sent directly to
 * the XMPPConnection object.
 * 
 * Singleton so that only one connection can exist at once, to prevent mistakes.
 */
public class ServerConnection {
	
	private static XMPPConnection connection;
	
	/**
	 * Private constructor.
	 */
	private ServerConnection() {
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns existing connected server instance, or creates and configures a
	 * new connection (and connects) if none is available. Always tries to
	 * connect the connection object to the server.
	 * 
	 * @return singleton XMPPConnection.
	 */
	public static XMPPConnection getConnection() {
		// Create new connection if none exists. Otherwise returns existing.
		if (connection == null) {
			// Gets server info from application resources
			Resources res = AardvarkApp.getContext().getResources();
			String server_addr = res.getString(edu.chalmers.aardvark.R.string.server_address);
			int server_port = res.getInteger(edu.chalmers.aardvark.R.integer.server_port);

			// Configures server. Disables some security settings that doesn't
			// work correctly on Android.
			ConnectionConfiguration config = new ConnectionConfiguration(server_addr, server_port);
			config.setSASLAuthenticationEnabled(false);
			config.setSelfSignedCertificateEnabled(false);
			config.setSecurityMode(SecurityMode.disabled);

			connection = new XMPPConnection(config);

			// Tries to connect to the server. Should only fail if server is
			// down or if device doesn't have a network connection.
			try {
				connection.connect();
				Log.i("INFO", "Connected to server!");
			} catch (XMPPException e) {
				Log.i("INFO", "Could not connect to server..." + e.getXMPPError());
			}

			// Listen for received packages, and set up package sender service
			AardvarkApp.getContext().startService(
					new Intent(AardvarkApp.getContext(), MessageSender.class));
			AardvarkApp.getContext().startService(
					new Intent(AardvarkApp.getContext(), MessageReceiver.class));
		}
		return connection;
	}

	/**
	 * Logs in to the server with an existing account.
	 * 
	 * @param username
	 *            the username to log into. Typically the AardvarkID.
	 * @param password
	 *            the password for the account.
	 * 
	 * @throws XMPPException
	 *             when log in failed. Important to catch and log the error
	 *             code.
	 */
	public static void login(String username, String password) throws XMPPException {
		// Make sure connection wasn't lost.
		if (!connection.isConnected()) {
			connection.connect();
		}

		Log.i("INFO", "Logging in user " + username);
		connection.login(username, password);
		Log.i("INFO", "Logged in!");

		// Accept all incoming requests for presence information
		// and start listening for presence changes in new service
		AardvarkApp.getContext().startService(
				new Intent(AardvarkApp.getContext(), StatusChecker.class));
		Log.i("INFO", "Added statuschecker.");
	}

	/**
	 * Kills the current server connection and creates a new. WARNING: Will log
	 * out user.
	 * 
	 * Mainly called in frustration because server won't cooperate -- typically
	 * when logging out, deleting accounts, registering new accounts and logging
	 * in rapidly, or simply when trying to delete an account which is somehow a
	 * problem.
	 */
	public static void restart() {
		connection.disconnect();
		connection = null;
		getConnection();
	}

	/**
	 * Registers an account on the server.
	 * 
	 * @param username
	 *            the username to register. Typically the user's AardvarkID.
	 * @param password
	 *            the password to assign to the user.
	 * @param nameAttribute
	 *            the attribute to add to the name field of the account. Used
	 *            for storing the user alias.
	 * 
	 * @throws XMPPException
	 *             when registration failed. Important to catch error code.
	 *             Common code is conflict (409), which means that an account
	 *             with the given username already exists.
	 */
	public static void register(String username, String password, String nameAttribute)
			throws XMPPException {
		// Make sure connection wasn't lost.
		if (!connection.isConnected()) {
			connection.connect();
		}
		Log.i("INFO", "Registring account " + username + "::" + password + " for alias: "
				+ nameAttribute);

		// Sets the name attribute of the account
		Map<String, String> aliasAttribute = new HashMap<String, String>();
		aliasAttribute.put("name", nameAttribute);

		// Creates account on server.
		connection.getAccountManager().createAccount(username, password, aliasAttribute);
		Log.i("INFO", "Registred!");
	}

}
