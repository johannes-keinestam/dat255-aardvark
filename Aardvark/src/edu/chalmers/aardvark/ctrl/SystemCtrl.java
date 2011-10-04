package edu.chalmers.aardvark.ctrl;

import java.util.Map;
import java.util.UUID;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ServerConnection;

public class SystemCtrl {
    private static SystemCtrl instance;

    public static SystemCtrl getCtrl() {
	if (instance == null) {
	    instance = new SystemCtrl();
	}
	return instance;
    }

    public void performStartUpDuty() {
	if (isFirstRun()) {
	    performSetup();
	}

	ChatCtrl.getInstance();
	ContactCtrl contactCtrl = ContactCtrl.getInstance();
	
	// Loads contacts from file
	SharedPreferences savedContacts = AardvarkApp.getContext()
		.getSharedPreferences("contacts", 0);
	for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
	    contactCtrl.addContact((String)entry.getValue(), entry.getKey());
	}

	// Loads local user unique identifiers/server details from file
	SharedPreferences savedLocalUser = AardvarkApp.getContext()
		.getSharedPreferences("localuser", 0);
	String aardvarkID = savedLocalUser.getString("ID", null);
	String password = savedLocalUser.getString("password", null);
	LocalUser.createUser(aardvarkID, password);

	// Set up and create new connection to server.
	XMPPConnection connection = ServerConnection.getConnection();
    }

    public void performShutDownDuty() {
	// Remove online roster
	Roster roster = ServerConnection.getConnection().getRoster();
	for (RosterEntry re : roster.getEntries()) {
	    try {
		roster.removeEntry(re);
	    } catch (XMPPException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	// Kill connection to server
	ServerConnection.kill();
    }

    private void performSetup() {
	// generate unique hashed device ID for user identification (Aardvark
	// ID)
	final TelephonyManager tm = (TelephonyManager) AardvarkApp.getContext()
		.getSystemService(Context.TELEPHONY_SERVICE);

	final String tmDevice, tmSerial, tmPhone, androidId;
	tmDevice = "" + tm.getDeviceId();
	tmSerial = "" + tm.getSimSerialNumber();
	androidId = ""
		+ android.provider.Settings.Secure.getString(AardvarkApp
			.getContext().getContentResolver(),
			android.provider.Settings.Secure.ANDROID_ID);

	UUID deviceUuid = new UUID(androidId.hashCode(),
		((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
	String aardvarkID = deviceUuid.toString();

	// generate pseudo-random password
	String genPassword = UUID.randomUUID().toString();

	// save AardvarkID and password as persistent data
	SharedPreferences savedLocalUser = AardvarkApp.getContext()
		.getSharedPreferences("localuser", 0);
	SharedPreferences.Editor editor = savedLocalUser.edit();

	editor.putString("ID", aardvarkID);
	editor.putString("password", genPassword);
	editor.commit();

	// TODO generate encryption keys, save

	// indicate that the application is now setup, for future use
	SharedPreferences settings = AardvarkApp.getContext()
		.getSharedPreferences("settings", 0);
	SharedPreferences.Editor settingsEditor = settings.edit();

	settingsEditor.putBoolean("firstRun", false);
	settingsEditor.commit();

    }

    private boolean isFirstRun() {
	Log.e("FFEEEEL", "REQUESTING SHARED PREFERENCES");
	SharedPreferences settings = AardvarkApp.getContext()
		.getSharedPreferences("settings", 0);
	Log.e("FFEEEEL", "DONE!!!!!!!!!!!!!!!!!");

	return settings.getBoolean("firstRun", true);
    }
}
