package edu.chalmers.aardvark.ctrl;

import edu.chalmers.aardvark.model.User;

public class UserCtrl {
    
    private static UserCtrl instance;

    private UserCtrl() {
    }

    public static UserCtrl getInstance() {
	if (instance == null) {
	    instance = new UserCtrl();
	}
	return instance;
    }
    public boolean isOnline(User user){
	return true;
    }
    
    
}
