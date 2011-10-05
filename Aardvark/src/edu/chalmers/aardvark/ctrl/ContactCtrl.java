package edu.chalmers.aardvark.ctrl;

import java.util.List;

import android.util.Log;
import edu.chalmers.aardvark.model.ActiveChatContainer;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.ContactsContainer;

public class ContactCtrl {
    private static ContactCtrl instance;
    private ContactsContainer contactList;

    private ContactCtrl() {
	contactList = new ContactsContainer();
	Log.i("INFO", this.toString() + " STARTED");
    }

    public static ContactCtrl getInstance() {
	if (instance == null) {
	    instance = new ContactCtrl();
	}
	return instance;
    }

    public void addContact(String nickname, String aardvarkID) {
	contactList.addContact(new Contact(nickname, aardvarkID));
	ServerHandlerCtrl.getInstance().subscribeToUserPresence(aardvarkID);
    }
    
    public void setNickname(String aardvarkID) {
	
    }

    public void openChat() {
    }
    
    public List<Contact> getContacts() {
	return contactList.getList();
    }

}
