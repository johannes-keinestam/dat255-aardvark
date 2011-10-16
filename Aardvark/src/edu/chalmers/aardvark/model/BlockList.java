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

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

/**
 * Model class used for keeping track of blocked user. Internally consists of a
 * list of User objects.
 * 
 * Sends events to GUI via ComBus.
 */
public class BlockList {
	/** List of Users currently blocked */
	private List<User> blockedUsers;

	/**
	 * Constructor. Initializes the list.
	 */
	public BlockList() {
		blockedUsers = new ArrayList<User>();
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Adds a user to list of blocked user.
	 * 
	 * Sends USER_BLOCKED StateChange on ComBus when done.
	 * 
	 * @param user
	 *            User object to add to list.
	 */
	public void addUser(User user) {
		blockedUsers.add(user);
		ComBus.notifyListeners(StateChanges.USER_BLOCKED.toString(), user);
	}

	/**
	 * Removes a user from list of blocked users.
	 * 
	 * Sends USER_UNBLOCKED StateChange on ComBus when done.
	 * 
	 * @param user
	 *            User object to remove.
	 */
	public void removeUser(User user) {
		blockedUsers.remove(user);
		ComBus.notifyListeners(StateChanges.USER_UNBLOCKED.toString(), user);
	}

	/**
	 * Finds a user on the list of blocked users.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return the associated blocked User, or null if it doesn't exist.
	 */
	public User findUser(String aardvarkID) {
		for (User user : blockedUsers) {
			if (user.getAardvarkID().equals(aardvarkID)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Returns list of blocked users.
	 * 
	 * @return list of User objects.
	 */
	public List<User> getBlockedUsers() {
		return blockedUsers;
	}

}
