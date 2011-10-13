package edu.chalmers.aardvark.ctrl;

import android.content.SharedPreferences;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.User;

public class SettingsCtrl {
    private static SettingsCtrl instance;

    private SettingsCtrl() {
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public static SettingsCtrl getInstance() {
	if (instance == null) {
	    instance = new SettingsCtrl();
	}
	return instance;
    }
    
    public void saveContact(Contact contact) {
	SharedPreferences savedContacts = AardvarkApp.getContext()
		.getSharedPreferences("contacts", 0);
	SharedPreferences.Editor contactEditor = savedContacts.edit();
	
	contactEditor.putString(contact.getAardvarkID(), contact.getNickname());
	contactEditor.commit();
    }
    
    public void deleteContact(Contact contact) {
	SharedPreferences savedContacts = AardvarkApp.getContext()
        	.getSharedPreferences("contacts", 0);
        SharedPreferences.Editor contactEditor = savedContacts.edit();
        
        contactEditor.remove(contact.getAardvarkID());
        contactEditor.commit();
    }
    
    public void renameContact(Contact contact, String newNickname) {
	SharedPreferences savedContacts = AardvarkApp.getContext()
        	.getSharedPreferences("contacts", 0);
        SharedPreferences.Editor contactEditor = savedContacts.edit();
        
        contactEditor.putString(contact.getAardvarkID(), newNickname);
        contactEditor.commit();	
    }
    
    public void saveBlockedUser(User user) {
    	SharedPreferences blockedUsers = AardvarkApp.getContext()
    		.getSharedPreferences("blocklist", 0);
    	SharedPreferences.Editor blocklistEditor = blockedUsers.edit();
    	
    	blocklistEditor.putString(user.getAardvarkID(), "");
    	blocklistEditor.commit();
    }
        
    public void deleteBlockedUser(User user) {
    	SharedPreferences blockedUsers = AardvarkApp.getContext()
            	.getSharedPreferences("blocklist", 0);
        SharedPreferences.Editor blocklistEditor = blockedUsers.edit();
            
        blocklistEditor.remove(user.getAardvarkID());
        blocklistEditor.commit();
    }
        
}
