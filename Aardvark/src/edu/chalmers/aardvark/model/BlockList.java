package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class BlockList {
    private List<User> blockedUsers;

    public BlockList() {
	blockedUsers = new ArrayList<User>();
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public void addUser(User user) {
	blockedUsers.add(user);
	ComBus.notifyListeners(StateChanges.USER_BLOCKED.toString(), user);
    }
    
    public void removeUser(User user) {
	blockedUsers.remove(user);
	ComBus.notifyListeners(StateChanges.USER_UNBLOCKED.toString(), user);
    }
    
    public User findUser(String aardvarkID) {
	for (User user : blockedUsers) {
	    if (user.getAardvarkID().equals(aardvarkID)) {
		return user;
	    }
	}
	return null;
    }
    
    public List<User> getBlockedUsers(){
    	return blockedUsers;
    }

}
