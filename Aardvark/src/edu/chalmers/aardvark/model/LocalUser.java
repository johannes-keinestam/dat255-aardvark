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

package edu.chalmers.aardvark.model;

import android.util.Log;

/**
 * Model class representing the local user, i.e. the user currently logged into
 * the app. The user is uniquely identifiable by their AardvarkID. Also has a
 * generated password used for logging into the server. When logged in, an alias
 * is also assigned to the user.
 */
public class LocalUser {
	/**
	 * The User object containing the alias and AardvarkID information, just
	 * like any other user
	 */
	private static User localUser;
	/** The password used to log into the server. Assigned at start up. */
	private static String password;

	/**
	 * Static method creating a user (there can only be one). Called at start up
	 * when saved the AardvarkID and password are loaded.
	 * 
	 * @param aardvarkID
	 *            AardvarkID assigned to the device.
	 * @param pass
	 *            password assigned to the AardvarkID.
	 */
	public static void createUser(String aardvarkID, String pass) {
		localUser = new User(null, aardvarkID);
		password = pass;
		Log.i("CLASS", "Localuser started!");
	}

	/**
	 * Sets the alias of the local user. Called when logging in.
	 * 
	 * @param alias
	 *            the alias to assign to the local user.
	 */
	public static void setAlias(String alias) {
		localUser.setAlias(alias);
	}

	/**
	 * Returns the user describing the public information of the user, i.e.
	 * alias and AardvarkID.
	 * 
	 * @return User object of local user.
	 */
	public static User getLocalUser() {
		return localUser;
	}

	/**
	 * Gets the password of the local user.
	 * 
	 * @return assigned password.
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * Clears the local user. Not used, but may be if the AardvarkID or password
	 * would be changed.
	 */
	public static void removeUser() {
		localUser = null;
	}

}
