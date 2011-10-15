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

import android.content.SharedPreferences;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.User;

/**
 * Controller class which handles saving and editing settings and data on the
 * device disk.
 */
public class SettingsCtrl {
	/** Static variable for singleton instance */
	private static SettingsCtrl instance;

	/**
	 * Private constructor.
	 */
	private SettingsCtrl() {
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of SettingsCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static SettingsCtrl getInstance() {
		if (instance == null) {
			instance = new SettingsCtrl();
		}
		return instance;
	}

	/**
	 * Saves the given contact on the device disk.
	 * 
	 * @param contact
	 *            contact whose data to save.
	 */
	public void saveContact(Contact contact) {
		SharedPreferences savedContacts = AardvarkApp.getContext().getSharedPreferences("contacts",
				0);
		SharedPreferences.Editor contactEditor = savedContacts.edit();

		contactEditor.putString(contact.getAardvarkID(), contact.getNickname());
		contactEditor.commit();
	}

	/**
	 * Deletes the given contact from the device disk.
	 * 
	 * @param contact
	 *            contact whose data to delete.
	 */
	public void deleteContact(Contact contact) {
		SharedPreferences savedContacts = AardvarkApp.getContext().getSharedPreferences("contacts",
				0);
		SharedPreferences.Editor contactEditor = savedContacts.edit();

		contactEditor.remove(contact.getAardvarkID());
		contactEditor.commit();
	}

	/**
	 * Edits the nickname field for the given contact on the device disk.
	 * 
	 * @param contact
	 *            contact whose nickname to edit.
	 * @param newNickname
	 *            the new nickname to save.
	 */
	public void renameContact(Contact contact, String newNickname) {
		SharedPreferences savedContacts = AardvarkApp.getContext().getSharedPreferences("contacts",
				0);
		SharedPreferences.Editor contactEditor = savedContacts.edit();

		contactEditor.putString(contact.getAardvarkID(), newNickname);
		contactEditor.commit();
	}

	/**
	 * Saves the given user on block list on the device disk.
	 * 
	 * @param user
	 *            user whose data to save on block list.
	 */
	public void saveBlockedUser(User user) {
		SharedPreferences blockedUsers = AardvarkApp.getContext().getSharedPreferences("blocklist",
				0);
		SharedPreferences.Editor blocklistEditor = blockedUsers.edit();

		blocklistEditor.putString(user.getAardvarkID(), "");
		blocklistEditor.commit();
	}

	/**
	 * Deletes the given user from block list on the device disk.
	 * 
	 * @param user
	 *            user whose data to delete from block list.
	 */
	public void deleteBlockedUser(User user) {
		SharedPreferences blockedUsers = AardvarkApp.getContext().getSharedPreferences("blocklist",
				0);
		SharedPreferences.Editor blocklistEditor = blockedUsers.edit();

		blocklistEditor.remove(user.getAardvarkID());
		blocklistEditor.commit();
	}

}
