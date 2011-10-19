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

import android.util.Log;
import edu.chalmers.aardvark.model.BlockList;
import edu.chalmers.aardvark.model.User;

/**
 * Controller class which handles blocking of users.
 */
public class UserCtrl {
	/** BlockList, data storage of all blocked users */
	private BlockList blocklist;
	/** Static variable for singleton instance */
	private static UserCtrl instance;

	/**
	 * Private constructor.
	 */
	private UserCtrl() {
		blocklist = new BlockList();
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of UserCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static UserCtrl getInstance() {
		if (instance == null) {
			instance = new UserCtrl();
		}
		return instance;
	}

	/**
	 * Adds the given user to block list. No messages sent from user will be
	 * received.
	 * 
	 * @param user
	 *            user to block.
	 */
	public void blockUser(User user) {
		// Save blocked user to disk.
		SettingsCtrl.getInstance().saveBlockedUser(user);
		blocklist.addUser(user);
	}

	/**
	 * Adds the given AardvarkID to block list. No messages sent from user will
	 * be received.
	 * 
	 * @param user
	 *            AardvarkID to block.
	 */
	public void blockUser(String aardvarkID) {
		// Save blocked user to disk.
		User blockedUser = new User(null, aardvarkID);
		blockUser(blockedUser);
	}

	/**
	 * Removes the given user from block list. Messages sent from user will be
	 * received.
	 * 
	 * @param user
	 *            user to unblock.
	 */
	public void unblockUser(User user) {
		// Remove blocked user from disk.
		SettingsCtrl.getInstance().deleteBlockedUser(user);
		blocklist.removeUser(user);
	}

	/**
	 * Removes the given AardvarkID from block list. Messages sent from user
	 * will be received.
	 * 
	 * @param aardvarkID
	 *            AardvarkID to unblock.
	 */
	public void unblockUser(String aardvarkID) {
		// Remove blocked user from disk.
		User unblockedUser = blocklist.findUser(aardvarkID);
		unblockUser(unblockedUser);
	}

	/**
	 * Returns whether a user is blocked or not.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return true if user is found on block list, false if not.
	 */
	public boolean isUserBlocked(String aardvarkID) {
		return (blocklist.findUser(aardvarkID) != null);
	}
}
