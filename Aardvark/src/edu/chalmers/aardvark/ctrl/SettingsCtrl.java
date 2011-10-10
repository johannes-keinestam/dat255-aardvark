package edu.chalmers.aardvark.ctrl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.Contact;

public class SettingsCtrl {
    private static SettingsCtrl instance;
    Context context = AardvarkApp.getContext();

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
}
