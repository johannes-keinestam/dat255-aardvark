package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class ContactsContainer {
    private List<Contact> contactList;

    public ContactsContainer() {
	contactList = new ArrayList<Contact>();
	Log.i("INFO", this.toString() + " STARTED");
    }

    public void addContact(Contact contact) {
	contactList.add(contact);
	ComBus.notifyListeners(StateChanges.CONTACT_ADDED.toString(), contact);
    }

    public void removeContact(Contact contact) {
	contactList.remove(contact);
	ComBus.notifyListeners(StateChanges.CONTACT_REMOVED.toString(), contact);
    }

    public Contact findContact(String nickname) {
	for (Contact c : contactList) {
	    if (c.getNickname().equals(nickname)) {
		return c;
	    }
	}
	return null;
    }

    public Contact findContactByID(String aardvarkID) {
	for (Contact c : contactList) {
	    if (c.getAardvarkID().equals(aardvarkID)) {
		return c;
	    }
	}
	return null;
    }

}
