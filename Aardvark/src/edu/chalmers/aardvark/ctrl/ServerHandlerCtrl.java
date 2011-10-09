package edu.chalmers.aardvark.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;

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
	Log.i("INFO", this.toString() + " STARTED");
    }

    public static ServerHandlerCtrl getInstance() {
	if (instance == null) {
	    instance = new ServerHandlerCtrl();
	}
	return instance;
    }

    public void subscribeToUserPresence(String aardvarkID) {
	Roster roster = ServerConnection.getConnection().getRoster();
	try {
	    roster.createEntry(aardvarkID, aardvarkID, null);
	} catch (XMPPException e) {
	    Toast.makeText(AardvarkApp.getContext(), e.getMessage().toString(),
		    Toast.LENGTH_LONG);
	}
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
	LocalUser.setAlias(alias);
	
	try {
	    String aardvarkID = LocalUser.getLocalUser().getAardvarkID();
	    String password = LocalUser.getPassword();
	    
	    Log.i("INFO", "Logging in, trying to register...");
	    ServerConnection.register(aardvarkID, password, alias);
	    Log.i("INFO", "Logging in, registered and try trying to log in...");
	    ServerConnection.login(LocalUser.getLocalUser().getAardvarkID(),
	    	LocalUser.getPassword());
	    Log.i("INFO", "Done logging in!");
	    
	    ComBus.notifyListeners(StateChanges.LOGGED_IN.toString(), null);
	} catch (XMPPException e) {
	    Log.i("INFO", e.getMessage());
	    if (e.getMessage().contains("conflict")) {
		try {
		    Log.i("INFO", "Login error! "+e.getMessage());
		    Log.i("INFO", "Logging into existing account..");
		    ServerConnection.getConnection().login(LocalUser.getLocalUser().getAardvarkID(), LocalUser.getPassword());
		    Log.i("INFO", "Deleting account...");
		    ServerConnection.getConnection().getAccountManager().deleteAccount();
		    logInWithAlias(alias);
		} catch (XMPPException e1) {
		    ComBus.notifyListeners(StateChanges.LOGIN_FAILED.toString(), null);
		}
	    } else {
		ComBus.notifyListeners(StateChanges.LOGIN_FAILED.toString(), null);
	    }
	}
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
	UserSearchManager search = new UserSearchManager(connection);
	ReportedData aliasData = null;
	try {
	    Form searchForm = search
		    .getSearchForm(AardvarkApp
			    .getContext()
			    .getString(
				    edu.chalmers.aardvark.R.string.server_search_address));
	    Form answerForm = searchForm.createAnswerForm();
	    answerForm.setAnswer("name", alias);
	    aliasData = search
		    .getSearchResults(
			    answerForm,
			    AardvarkApp
				    .getContext()
				    .getString(
					    edu.chalmers.aardvark.R.string.server_search_address));
	} catch (XMPPException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (aliasData == null || !aliasData.getRows().hasNext()) {
	    return null;
	} 
	
	String aardvarkID = aliasData.getRows().next().getValues("username").toString();
	return aardvarkID;
    }
    
    public String getAlias(String aardvarkID) {
	UserSearchManager search = new UserSearchManager(connection);
	ReportedData idData = null;
	try {
	    Form searchForm = search
		    .getSearchForm(AardvarkApp
			    .getContext()
			    .getString(
				    edu.chalmers.aardvark.R.string.server_search_address));
	    Form answerForm = searchForm.createAnswerForm();
	    answerForm.setAnswer("username", aardvarkID);
	    idData = search
		    .getSearchResults(
			    answerForm,
			    AardvarkApp
				    .getContext()
				    .getString(
					    edu.chalmers.aardvark.R.string.server_search_address));
	} catch (XMPPException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (idData == null || !idData.getRows().hasNext()) {
	    return null;
	} 
	
	String alias = idData.getRows().next().getValues("alias").toString();
	return alias;	
    }
}
