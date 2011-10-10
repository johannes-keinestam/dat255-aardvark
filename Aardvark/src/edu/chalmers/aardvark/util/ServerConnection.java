package edu.chalmers.aardvark.util;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ToContainsFilter;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.services.MessageReceiver;
import edu.chalmers.aardvark.services.MessageSender;
import edu.chalmers.aardvark.services.StatusChecker;

public class ServerConnection {
    private static XMPPConnection connection;
    private static MessageSender sender;

    private ServerConnection() {
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public static XMPPConnection getConnection() {
	if (connection == null) {
	    // Gets server info from application resources
	    Resources res = AardvarkApp.getContext().getResources();
	    String server_addr = res
		    .getString(edu.chalmers.aardvark.R.string.server_address);
	    int server_port = res
		    .getInteger(edu.chalmers.aardvark.R.integer.server_port);
	    
	    // connect to server
	    ConnectionConfiguration config = new ConnectionConfiguration(
		    server_addr, server_port);
	    config.setSASLAuthenticationEnabled(false);	
	    config.setSelfSignedCertificateEnabled(false);
	    config.setSecurityMode(SecurityMode.disabled);

	    connection = new XMPPConnection(config);
	    
	    try {
		connection.connect();
		Log.i("INFO", "Connected to server!");
	    } catch (XMPPException e) {
		Toast.makeText(AardvarkApp.getContext(), e.getXMPPError()
			.toString(), Toast.LENGTH_LONG);
	    }

	    // listen for received packages, and set up package sender service
	    // class
	    AardvarkApp.getContext().startService(new Intent(AardvarkApp.getContext(), MessageSender.class));
	    AardvarkApp.getContext().startService(new Intent(AardvarkApp.getContext(), MessageReceiver.class));
	}
	return connection;
    }

    public static void login(String aardvarkID, String password) throws XMPPException {
	if (!connection.isConnected()) {
	    connection.connect();
	}
	    	Log.i("INFO", "Logging in user " + aardvarkID);
	    	connection.login(aardvarkID, password);
	    	Log.i("INFO", "Logged in!");

	    // accept all incoming requests for my presence information
	    // and start listening for presence changes in new service
	    AardvarkApp.getContext().startService(new Intent(AardvarkApp.getContext(), StatusChecker.class));
	    Log.i("INFO", "Added statuschecker.");
    }

    public static void restart() {
	connection.disconnect();
	connection = null;
	getConnection();
    }
    
    public static void register(String aardvarkID, String password, String alias) throws XMPPException {
    	Log.i("INFO", "Registring account " + aardvarkID+"::"+password+" for alias: "+alias);
    	
	Map<String, String> aliasAttribute = new HashMap<String, String>();
	aliasAttribute.put("name", alias);
	
    	connection.getAccountManager().createAccount(aardvarkID, password, aliasAttribute);
    	Log.i("INFO", "Registred!");
    }

}
