package edu.chalmers.aardvark.ctrl;

import android.util.Log;
import edu.chalmers.aardvark.model.BlockList;
import edu.chalmers.aardvark.model.User;

public class UserCtrl {
    private BlockList blocklist;
    private static UserCtrl instance;
    
    private UserCtrl() {
	blocklist = new BlockList();
	Log.i("CLASS", this.toString() + " STARTED");
    }

    public static UserCtrl getInstance() {
	if (instance == null) {
	    instance = new UserCtrl();
	}
	return instance;
    }
    
    public void blockUser(User user) {
	blocklist.addUser(user);
    }
    
    public void blockUser(String aardvarkID) {
	User blockedUser = new User(null, aardvarkID);
	blockUser(blockedUser);
    }
    
    public void unblockUser(User user) {
	blocklist.removeUser(user);
    }
    
    public void unblockUser(String aardvarkID) {
	User unblockedUser = blocklist.findUser(aardvarkID);
	blocklist.removeUser(unblockedUser);
    }
    
    public boolean isUserBlocked(String aardvarkID) {
	if (blocklist.findUser(aardvarkID) == null) {
	    return false;
	} else {
	    return true;
	}
    }
}
