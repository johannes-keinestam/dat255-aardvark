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

import java.util.List;

import android.util.Log;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.ContactsContainer;

/**
 * Controller class which handles all contact related functionality.
 */
public class ContactCtrl {
	/** Static variable for singleton instance */
	private static ContactCtrl instance;
	/** ContactContainer, data storage of all contacts */
	private ContactsContainer contactList;

	/**
	 * Private constructor.
	 */
	private ContactCtrl() {
		contactList = new ContactsContainer();
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of ContactCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static ContactCtrl getInstance() {
		if (instance == null) {
			instance = new ContactCtrl();
		}
		return instance;
	}

	/**
	 * Adds a contact to contact list and saves it to device disk for use next
	 * time.
	 * 
	 * @param nickname
	 *            the nickname to assign to the contact.
	 * @param aardvarkID
	 *            the AardvarkID to assign nickname to.
	 */
	public void addContact(String nickname, String aardvarkID) {
		Contact contact = new Contact(nickname, aardvarkID);

		// Saving added contact to disk
		SettingsCtrl.getInstance().saveContact(contact);
		contactList.addContact(contact);
	}

	/**
	 * Removes a contact from contact list and device disk.
	 * 
	 * @param aardvarkID
	 *            the contact's AardvarkID
	 */
	public void removeContact(String aardvarkID) {
		Contact contact = contactList.findContactByID(aardvarkID);

		// Deleting contact from disk
		SettingsCtrl.getInstance().deleteContact(contact);
		contactList.removeContact(contactList.findContactByID(aardvarkID));
	}

	/**
	 * Removes all contacts.
	 */
	public void removeContacts() {
		contactList.getList().clear();
	}

	/**
	 * Sets a new nickname for the given user and saves it to device disk.
	 * 
	 * @param aardvarkID
	 *            the contact AardvarkID whose nickname to change.
	 * @param newNickname
	 *            the new nickname to assign.
	 */
	public void setNickname(String aardvarkID, String newNickname) {
		Contact contact = contactList.findContactByID(aardvarkID);

		// Renames contact on disk
		SettingsCtrl.getInstance().renameContact(contact, newNickname);
		contact.rename(newNickname);
	}

	/**
	 * Gets all currently added contacts from ContactContainer.
	 * 
	 * @return list of Contact objects currently added.
	 */
	public List<Contact> getContacts() {
		return contactList.getList();
	}

	/**
	 * Gets the Contact object associated with the given AardvarkID.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return the associated Contact, or null if it doesn't exist.
	 */
	public Contact getContact(String aardvarkID) {
		for (Contact contact : contactList.getList()) {
			if (contact.getAardvarkID().equals(aardvarkID)) {
				return contact;
			}
		}
		return null;
	}

	/**
	 * Checks if the user with a given AardvarkID is a contact.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return true if the AardvarkID is associated with a contact, false if
	 *         not.
	 */
	public boolean isContact(String aardvarkID) {
		List<Contact> contacts = getContacts();
		for (Contact contact : contacts) {
			if (contact.getAardvarkID().equals(aardvarkID)) {
				return true;
			}
		}
		return false;

	}

}
