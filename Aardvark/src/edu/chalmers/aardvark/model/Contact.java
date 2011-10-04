package edu.chalmers.aardvark.model;

import android.util.Log;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class Contact extends User {
    private String nickname;

    public Contact(String nickname, String aardvarkID) {
	super(null, aardvarkID);
	this.nickname = nickname;
	Log.i("INFO", this.toString() + " STARTED");
    }

    public String getNickname() {
	return nickname;
    }

    public void rename(String newNickname) {
	nickname = newNickname;
	ComBus.notifyListeners(StateChanges.CONTACT_RENAMED.toString(), this);
    }

}
