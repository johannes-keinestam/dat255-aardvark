package edu.chalmers.aardvark.util;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.content.res.Resources;
import android.widget.Toast;

import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.services.StatusChecker;

public class ServerConnection {
	private static XMPPConnection connection;
	
	public ServerConnection() {
		//Gets application resources
		Resources res = AardvarkApp.getContext().getResources();
		
		//getting server info
		String server_addr = res.getString(edu.chalmers.aardvark.R.string.server_address);
		int server_port = res.getInteger(edu.chalmers.aardvark.R.integer.server_port);
		
		ConnectionConfiguration config = new ConnectionConfiguration(server_addr, server_port);
		connection = new XMPPConnection(config);
		try {
			connection.connect();
			Toast.makeText(AardvarkApp.getContext(), "Connected to server!", Toast.LENGTH_LONG);
		} catch (XMPPException e) {
			Toast.makeText(AardvarkApp.getContext(), e.getXMPPError().toString(), Toast.LENGTH_LONG);
		}
		
		Roster roster = connection.getRoster();
		roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
		roster.addRosterListener(new StatusChecker());
	}
	
	public void login() {
		try {
			// TODO how to store?
			connection.login("", "");
		} catch (XMPPException e) {
			Toast.makeText(AardvarkApp.getContext(), e.getXMPPError().toString(), Toast.LENGTH_LONG);
		}
	}
	public static XMPPConnection getConnection() {
		return connection;
	}
	
	public void kill() {
		connection.disconnect();
	}
	
}
