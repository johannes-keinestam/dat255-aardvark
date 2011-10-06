package edu.chalmers.aardvark.util;

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
	Log.i("INFO", this.toString() + " STARTED");
    }

    public static XMPPConnection getConnection() {
	if (connection == null) {
	    // Gets application resources
	    Resources res = AardvarkApp.getContext().getResources();

	    // getting server info
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
		Toast.makeText(AardvarkApp.getContext(),
			"Connected to server!", Toast.LENGTH_LONG);
		Log.i("info", "connected");
		Log.i("info", config.isSASLAuthenticationEnabled()+"");
		
		 try {
				connection.getAccountManager().createAccount("testuser123", "password");
			} catch (XMPPException e1) {
				// TODO Auto-generated catch block
				Log.i("Info", "inget reggas"+e1.toString());
			}
		//SASLAuthentication.supportSASLMechanism("CRAM", 0);
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
	    connection.login(aardvarkID, password);

	    // accept all incoming requests for my presence information
	    // and start listening for presence changes in new service
	    AardvarkApp.getContext().startService(new Intent(AardvarkApp.getContext(), StatusChecker.class));
    }

    public static void kill() {
	connection.disconnect();
    }

}
