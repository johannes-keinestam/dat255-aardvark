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
 * Model class used for keeping track of users added as contacts. Internally
 * consists of a list of Contact objects.
 * 
 * Sends events to GUI via ComBus.
 */
public class ContactsContainer {
	/** List of added contacts */
	private List<Contact> contactList;

	/**
	 * Constructor initializing the list.
	 */
	public ContactsContainer() {
		contactList = new ArrayList<Contact>();
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Adds a contact to the list of contacts.
	 * 
	 * Sends CONTACT_ADDED StateChange on ComBus when done.
	 * 
	 * @param contact
	 *            Contact object to add.
	 */
	public void addContact(Contact contact) {
		contactList.add(contact);
		ComBus.notifyListeners(StateChanges.CONTACT_ADDED.toString(), contact);
	}

	/**
	 * Removes a contact from list of contacts.
	 * 
	 * Sends CONTACT_REMOVED StateChange on ComBus when done.
	 * 
	 * @param contact
	 *            Contact object to remove.
	 */
	public void removeContact(Contact contact) {
		contactList.remove(contact);
		ComBus.notifyListeners(StateChanges.CONTACT_REMOVED.toString(), contact);
	}

	/**
	 * Finds a contact added to contact list by nickname.
	 * 
	 * @param nickname
	 *            the nickname to search for.
	 * @return associated Contact object if found, null if not.
	 */
	public Contact findContact(String nickname) {
		for (Contact c : contactList) {
			if (c.getNickname().equals(nickname)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Finds a contact added to contact list by AardvarkID.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return associated Contact object if found, null if not.
	 */
	public Contact findContactByID(String aardvarkID) {
		for (Contact c : contactList) {
			if (c.getAardvarkID().equals(aardvarkID)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Gets list of all contacts currently on contact list.
	 * 
	 * @return list of Contact objects.
	 */
	public List<Contact> getList() {
		return contactList;
	}

}
