package edu.chalmers.aardvark.ctrl;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;
import android.widget.Toast;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;

public class ServerHandlerCtrl {
    private XMPPConnection connection = ServerConnection.getConnection();
    private static ServerHandlerCtrl instance;

    private ServerHandlerCtrl() {
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public static ServerHandlerCtrl getInstance() {
	if (instance == null) {
	    instance = new ServerHandlerCtrl();
	}
	return instance;
    }

    public boolean isOnline(User user) {
	Roster roster = ServerConnection.getConnection().getRoster();
	Presence presence = roster.getPresence(user.getAardvarkID());

	if (presence.isAvailable()) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean isAliasAvailable(String alias) {
	String aliasMatch = getAardvarkID(alias);
	if (aliasMatch == null) {
	    return true;
	} else {
	    return false;
	}
    }

    public void logInWithAlias(String alias) {
    try {
		LocalUser.setAlias(alias);
		
	    String aardvarkID = LocalUser.getLocalUser().getAardvarkID();
	    String password = LocalUser.getPassword();
	    Log.i("INFO", "Logging in, trying to register...");
	    ServerConnection.register(aardvarkID, password, alias);
	    Log.i("INFO", "Logging in, registered and try trying to log in...");
	    ServerConnection.login(aardvarkID, password);
	    
	    ComBus.notifyListeners(StateChanges.LOGGED_IN.toString(), null);
	} catch (XMPPException e) {
	    Log.i("INFO", e.getMessage());
		try {
		    Log.i("INFO", "Login error! "+e.getMessage());
		    Log.i("INFO", "Logging into existing account..");
		    ServerConnection.getConnection().login(LocalUser.getLocalUser().getAardvarkID(), LocalUser.getPassword());
		    Log.i("INFO", "Deleting account...");
		    ServerConnection.getConnection().getAccountManager().deleteAccount();
		    ServerConnection.restart();
		    
		    logInWithAlias(alias);
		} catch (XMPPException e1) {
		    ComBus.notifyListeners(StateChanges.LOGIN_FAILED.toString(), null);
		}
	}
    }
	
	public void deleteAccount(String aardvarkID, String password) {
		
	}
	
    public void logOut() {
	Log.i("INFO", "Logging out...");
	try {
	    ServerConnection.getConnection().getAccountManager().deleteAccount();
	} catch (XMPPException e) {
	    Log.i("INFO", "Could not delete account!");
	}

	ServerConnection.restart();
	Log.i("INFO", "Logged out!");
	ComBus.notifyListeners(StateChanges.LOGGED_OUT.toString(), null);
    }
    
    public String getAardvarkID(String alias) {
    	RosterGroup onlineUsers = ServerConnection.getConnection().getRoster().getGroup("Aardvark");
    	for (RosterEntry user : onlineUsers.getEntries()) {
    		if (user.getName().equals(alias)) {
    			String username = user.getUser();
    			return username.substring(0, username.lastIndexOf("@"));
    		}
    		Log.i("INFO", "User in group, user: "+user.getUser());
    		Log.i("INFO", "User in group, namn: "+user.getName());
    	}
		return null;
    }
    
    public String getAlias(String aardvarkID) {
    	RosterGroup onlineUsers = ServerConnection.getConnection().getRoster().getGroup("Aardvark");
    	for (RosterEntry user : onlineUsers.getEntries()) {
    		String username = user.getUser();
    		String userAardvarkID = username.substring(0, username.lastIndexOf("@"));
    		if (userAardvarkID.equals(aardvarkID)) {
    			return user.getName();
    		}
    		Log.i("INFO", "User in group, user: "+user.getUser());
    		Log.i("INFO", "User in group, namn: "+user.getName());
    	}
		return null;

    }
}
