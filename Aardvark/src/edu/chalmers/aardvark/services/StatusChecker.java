package edu.chalmers.aardvark.services;

import java.util.Collection;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StatusChecker extends Service implements RosterListener {

	// TODO thread. timer. at alarm, check contact status and message gui.

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
	public void entriesAdded(Collection<String> arg0) {	}

	@Override
	public void entriesDeleted(Collection<String> arg0) { }

	@Override
	public void entriesUpdated(Collection<String> arg0) { }

}
