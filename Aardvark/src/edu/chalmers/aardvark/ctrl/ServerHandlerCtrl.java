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

package edu.chalmers.aardvark.ctrl;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;

/**
 * Controller class which handles all server related functionality.
 */
public class ServerHandlerCtrl {
	/** Static variable for singleton instance */
	private static ServerHandlerCtrl instance;
	/** Boolean which is keeps track if you are currently logged in or not. */
	private boolean isLoggedIn = false;

	/**
	 * Private constructor.
	 */
	private ServerHandlerCtrl() {
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of ServerHandlerCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static ServerHandlerCtrl getInstance() {
		if (instance == null) {
			instance = new ServerHandlerCtrl();
		}
		return instance;
	}

	/**
	 * Checks whether a user is online or not.
	 * 
	 * @param user
	 *            the user to check for (the alias is not important).
	 * @return true if user is online, false if not.
	 */
	public boolean isOnline(User user) {
		// Gets roster (list of statuses) from server, and gets presence
		// (status) of specified user.
		Roster roster = ServerConnection.getConnection().getRoster();
		Presence presence = roster.getPresence(user.getAardvarkID());

		if (presence.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether the given alias is available (i.e. if any user registered
	 * on server is using this as a name). Typically used when logging in, so
	 * two users using the same alias (note: not the same as username) isn't
	 * allowed.
	 * 
	 * @param alias
	 *            the alias to search for.
	 * @return true if alias is available, false if not or was unable to check.
	 */
	private boolean isAliasAvailable(String alias) {
		try {
			Log.i("INFO", "Checking status!");
			
			ServerConnection.getConnection();

			// Performs server reconnection so that the server will cooperate.
			// You will see a lot of these...
			ServerConnection.restart();

			// Logs in as dedicated status checker user to access roster
			ServerConnection.getConnection().login("statuschecker", "statuschecker");
			String matchingUser = getAardvarkID(alias);
			Log.i("INFO", "Done checking, disconnecting. RESULT: " + matchingUser);

			ServerConnection.restart();

			// If no matching user found, or matching user is yourself, alias is
			// available.
			if (matchingUser == null
					|| matchingUser.equals(LocalUser.getLocalUser().getAardvarkID())) {
				return true;
			} else {
				return false;
			}
		} catch (XMPPException e) {
			// Server connection error.
			Log.i("INFO", "Could not check status! " + e.getMessage());
		}
		return false;
	}

	/**
	 * Logs in with the given alias in a new thread. This is done using the
	 * AardvarkID as the username, and the generated password for password.
	 * Alias is set as the optional "name" variable on the server.
	 * 
	 * Will do nothing if alias is unavailable.
	 * 
	 * @param alias
	 *            the alias to log in with.
	 */
	public void logInWithAlias(final String alias) {
		// Starts new thread to make GUI more responsive.
		new Thread(new Runnable() {
			public void run() {
				// Only log in if alias is available.
				if (isAliasAvailable(alias)) {
					// Try three times to log in at most.
					for (int i = 0; i < 3; i++) {
						try {
							LocalUser.setAlias(alias);
							String aardvarkID = LocalUser.getLocalUser().getAardvarkID();
							String password = LocalUser.getPassword();
							Log.i("INFO", "Logging in, trying to register...");

							// Registering user to log in with.
							ServerConnection.register(aardvarkID, password, alias);
							Log.i("INFO", "Logging in, registered and try trying to log in...");

							// Log in with user.
							ServerConnection.login(aardvarkID, password);
							isLoggedIn = true;
							ComBus.notifyListeners(StateChanges.LOGGED_IN.toString(), null);

							// Successfully logged in! No need to try again.
							break;
						} catch (XMPPException e) {
							// Could not log in, typically because user could
							// not register due to conflict with already
							// existing user.
							Log.i("INFO", "Login error! " + e.getMessage());
							ServerConnection.restart();
							try {
								// Try to log in to existing account and
								// deleting it.
								Log.i("INFO", "Logging into existing account..");
								ServerConnection.getConnection().login(
										LocalUser.getLocalUser().getAardvarkID(),
										LocalUser.getPassword());
								Log.i("INFO", "Deleting account...");
								ServerConnection.getConnection().getAccountManager()
										.deleteAccount();
							} catch (XMPPException e1) {
								// Server error. Could not delete account.
								Log.i("INFO", "Login error 2! " + e.getMessage());

							}
							// Non-successful login attempt, reconnect to server
							// and try again. You'd be surprised, it usually
							// works.
							ServerConnection.restart();
						}
					}

				} else {
					// Alias unavailable, notifying user.
					ComBus.notifyListeners(StateChanges.ALIAS_UNAVAILABLE.toString(), null);
				}
			}
		}).start();
	}

	/**
	 * Returns whether the user is currently logged in. Used for ignoring status
	 * updates from users if not logged in.
	 * 
	 * @return true if currently logged in, false if not.
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * Logs out from the server in a new thread. Tries to delete the account
	 * logged into before since a new account must be created at log in, because
	 * the server doesn't allow changing of the "name" variable used for storing
	 * alias.
	 */
	public void logOut() {
		// Starts new thread to make GUI more responsive.
		new Thread(new Runnable() {
			public void run() {
				Log.i("INFO", "Logging out...");
				isLoggedIn = false;

				ChatCtrl.getInstance().closeChats();

				try {
					// Trying to delete account, since a new must be registered
					// at next login.
					ServerConnection.getConnection().getAccountManager().deleteAccount();
				} catch (XMPPException e) {
					Log.i("INFO", "Could not delete account! " + e.getMessage());
				}

				ServerConnection.restart();
				Log.i("INFO", "Logged out!");
				ComBus.notifyListeners(StateChanges.LOGGED_OUT.toString(), null);
			}
		}).start();
	}

	/**
	 * Gets the AardvarkID associated with the given alias on the server.
	 * 
	 * @param alias
	 *            the alias to search for
	 * @return the matching AardvarkID, or null if alias doesn't exist.
	 */
	public String getAardvarkID(String alias) {
		// Searching roster group Aardvark (which all Aardvark users are added
		// to)
		RosterGroup onlineUsers = ServerConnection.getConnection().getRoster().getGroup("Aardvark");
		for (RosterEntry user : onlineUsers.getEntries()) {
			if (user.getName().equals(alias)) {
				String username = user.getUser();
				return username.substring(0, username.lastIndexOf("@"));
			}
		}
		return null;
	}

	/**
	 * Gets all registered users from the server.
	 * 
	 * @return collection of users as RosterEntry objects.
	 */
	public Collection<RosterEntry> getRegisteredUsers() {
		return ServerConnection.getConnection().getRoster().getGroup("Aardvark").getEntries();
	}

	/**
	 * Gets the alias associated with the given AardvarkID on the server.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return the matching alias, or null if none found.
	 */
	public String getAlias(String aardvarkID) {
		// Searching roster group Aardvark (which all Aardvark users are added
		// to)
		RosterGroup onlineUsers = ServerConnection.getConnection().getRoster().getGroup("Aardvark");
		for (RosterEntry user : onlineUsers.getEntries()) {
			String username = user.getUser();
			String userAardvarkID = username.substring(0, username.lastIndexOf("@"));
			if (userAardvarkID.equals(aardvarkID)) {
				return user.getName();
			}
		}
		return null;

	}
}
