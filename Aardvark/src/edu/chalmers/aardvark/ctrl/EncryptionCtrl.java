package edu.chalmers.aardvark.ctrl;

import android.util.Log;

public class EncryptionCtrl {
    private static EncryptionCtrl instance;

    private EncryptionCtrl() {
	Log.i("CLASS", this.toString() + " STARTED");

    }

    public static EncryptionCtrl getInstance() {
	if (instance == null) {
	    instance = new EncryptionCtrl();
	}
	return instance;
    }
}
