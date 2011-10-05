package edu.chalmers.aardvark.services;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;
import org.jivesoftware.smack.packet.Presence;

import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StatusChecker extends Service implements RosterListener {

    @Override
    public void onCreate() {
	XMPPConnection connection = ServerConnection.getConnection();
	
	Roster roster = connection.getRoster();
	roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
	roster.addRosterListener(this);
	
	Log.i("INFO", this.toString() + " STARTED");
    }

    @Override
    public void presenceChanged(Presence presence) {
	StateChanges statusChange;
	if (presence.isAvailable()) {
	    statusChange = StateChanges.USER_ONLINE;
	} else {
	    statusChange = StateChanges.USER_OFFLINE;
	}
	ComBus.notifyListeners(statusChange.toString(), presence.getFrom());
    }

    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }

    @Override
    public void entriesAdded(Collection<String> arg0) {
    }

    @Override
    public void entriesDeleted(Collection<String> arg0) {
    }

    @Override
    public void entriesUpdated(Collection<String> arg0) {
    }

}
