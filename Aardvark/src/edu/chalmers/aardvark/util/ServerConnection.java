package edu.chalmers.aardvark.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.content.res.Resources;
import android.widget.Toast;

import edu.chalmers.aardvark.AardvarkApp;

public class ServerConnection {
	private static XMPPConnection connection;
	
	public ServerConnection() {
		//Gets application context
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
	}
	
	public void login() {
		// TODO hur?
		try {
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
