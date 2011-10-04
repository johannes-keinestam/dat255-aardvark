package edu.chalmers.aardvark.ctrl;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.widget.Toast;

import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ServerConnection;

public class ServerHandlerCtrl {
	XMPPConnection connection = ServerConnection.getConnection();
	
	private ServerHandlerCtrl() {
		
	}
	
	public void subscribeToUserPresence(String aardvarkID) {
		Roster roster = connection.getRoster();
		try {
			roster.createEntry(aardvarkID, aardvarkID, null);
		} catch (XMPPException e) {
			Toast.makeText(AardvarkApp.getContext(), e.getMessage().toString(), Toast.LENGTH_LONG);
		}
	}
	
	public boolean isOnline(User user) {
		Roster roster = connection.getRoster();
		Presence presence = roster.getPresence(user.getAardvarkID());
		if (presence.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
}
