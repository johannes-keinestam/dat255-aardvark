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
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

/**
 * Model class representing a user added as contact.
 * 
 * Sends events to GUI via ComBus.
 */
public class Contact extends User {
	/** The nickname assigned to the contact */
	private String nickname;

	/**
	 * Constructor initializing the contact.
	 * 
	 * @param nickname
	 *            the nickname to assign to the contact.
	 * @param aardvarkID
	 *            the AardvarkID of the contact.
	 */
	public Contact(String nickname, String aardvarkID) {
		super(null, aardvarkID);
		this.nickname = nickname;
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Gets the nickname assigned to the contact.
	 * 
	 * @return nickname.
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Changes the nickname of the contact.
	 * 
	 * Sends CONTACT_RENAMED StateChange on ComBus when done.
	 * 
	 * @param newNickname
	 *            nickname to assign.
	 */
	public void rename(String newNickname) {
		nickname = newNickname;
		ComBus.notifyListeners(StateChanges.CONTACT_RENAMED.toString(), this);
	}

}
