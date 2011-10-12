package edu.chalmers.aardvark.ctrl;

import java.util.Collection;
import java.util.List;

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
    private boolean isLoggedIn = false;

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

    private boolean isAliasAvailable(String alias) {
	try {
	    Log.i("INFO", "Checking status!");
	    ServerConnection.getConnection().login("statuschecker", "statuschecker");
	    String matchingUser = getAardvarkID(alias);
	    Log.i("INFO", "Done checking, disconnecting. RESULT: "+matchingUser);
	    ServerConnection.restart();
	    if (matchingUser == null) {
		return true;
	    } else {
		return false;
	    }
	} catch (XMPPException e) {
	    Log.i("INFO", "Could not check status! "+e.getMessage());
	}
	return false;
    }

    public void logInWithAlias(String alias) {
    try {
	if (isAliasAvailable(alias)) {
	    LocalUser.setAlias(alias);
	    String aardvarkID = LocalUser.getLocalUser().getAardvarkID();
	    String password = LocalUser.getPassword();
	    Log.i("INFO", "Logging in, trying to register...");
	    ServerConnection.register(aardvarkID, password, alias);
	    Log.i("INFO", "Logging in, registered and try trying to log in...");
	    ServerConnection.login(aardvarkID, password);
	    isLoggedIn = true;
	    ComBus.notifyListeners(StateChanges.LOGGED_IN.toString(), null);
	} else {
	    ComBus.notifyListeners(StateChanges.ALIAS_UNAVAILABLE.toString(), null);
	}	
	    
	} catch (XMPPException e) {
		try {
		    Log.i("INFO", "Login error! "+e.getMessage());
		    Log.i("INFO", "Logging into existing account..");
		    ServerConnection.getConnection().login(LocalUser.getLocalUser().getAardvarkID(), LocalUser.getPassword());
		    Log.i("INFO", "Deleting account...");
		    ServerConnection.getConnection().getAccountManager().deleteAccount();
		    ServerConnection.restart();
		    
		    logInWithAlias(alias);
		} catch (XMPPException e1) {
		    Log.i("INFO", "Login error2! "+e.getMessage());
		    ComBus.notifyListeners(StateChanges.LOGIN_FAILED.toString(), null);
		}
	}
    }
	
	public boolean isLoggedIn() {
	    return isLoggedIn;
	}
	
    public void logOut() {
	Log.i("INFO", "Logging out...");
	isLoggedIn = false;
	ChatCtrl.getInstance().closeChats();
	try {
	    ServerConnection.getConnection().getAccountManager().deleteAccount();
	} catch (XMPPException e) {
	    Log.i("INFO", "Could not delete account! "+e.getMessage());
	    // 
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
    	}
		return null;
    }
    public Collection<RosterEntry> getOnlineUsers(){
    	return ServerConnection.getConnection().getRoster().getGroup("Aardvark").getEntries();
    }
    
    public String getAlias(String aardvarkID) {
    	RosterGroup onlineUsers = ServerConnection.getConnection().getRoster().getGroup("Aardvark");
    	for (RosterEntry user : onlineUsers.getEntries()) {
    		String username = user.getUser();
    		String userAardvarkID = username.substring(0, username.lastIndexOf("@"));
    		if (userAardvarkID.equals(aardvarkID)) {
    			return user.getName();
    		}
    	}
		return null;

    }
}
