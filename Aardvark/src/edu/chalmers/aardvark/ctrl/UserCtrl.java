package edu.chalmers.aardvark.ctrl;

import android.util.Log;
import edu.chalmers.aardvark.model.User;

public class UserCtrl {

    private static UserCtrl instance;

    private UserCtrl() {
	Log.i("INFO", this.toString() + " STARTED");
    }

    public static UserCtrl getInstance() {
	if (instance == null) {
	    instance = new UserCtrl();
	}
	return instance;
    }

    public boolean isOnline(User user) {
	return true;
    }

}
