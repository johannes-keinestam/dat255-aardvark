package edu.chalmers.aardvark.services;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;

public class StatusChecker extends Service implements RosterListener {

    @Override
    public void onCreate() {
	XMPPConnection connection = ServerConnection.getConnection();
	
	Roster roster = connection.getRoster();
	
	roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
	roster.addRosterListener(this);
	
	Log.i("CLASS", this.toString() + " STARTED");
    }

    @Override
    public void presenceChanged(Presence presence) {
	StateChanges statusChange;
	Log.i("STATUS", "PRESENCE CHANGED FOR USER: "+presence.getFrom());
	if (presence.isAvailable()) {
	    statusChange = StateChanges.USER_ONLINE;
	    Log.i("STATUS", "LOGGED IN: "+presence.getFrom());
	} else {
	    statusChange = StateChanges.USER_OFFLINE;
	    Log.i("STATUS", "LOGGED OUT: "+presence.getFrom());
	}
	String from = presence.getFrom();
	String aardvarkID = from.substring(0, from.lastIndexOf("@"));
	ComBus.notifyListeners(statusChange.toString(), aardvarkID);
    }

    @Override
    public void entriesAdded(Collection<String> addresses) {
	for (String address : addresses) {
	    Log.i("STATUS", "USER ADDED, SETTING ONLINE: "+address);
	    String aardvarkID = address.substring(0, address.lastIndexOf("@"));
	    ComBus.notifyListeners(StateChanges.LOGGED_IN.toString(), aardvarkID);
	}
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
	for (String address : addresses) {
	    Log.i("STATUS", "USER DELETED, SETTING OFFLINE: "+address);
	    String aardvarkID = address.substring(0, address.lastIndexOf("@"));
	    ComBus.notifyListeners(StateChanges.LOGGED_IN.toString(), aardvarkID);
	}
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
    }

    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }

}
