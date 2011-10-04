package edu.chalmers.aardvark.ctrl;

import android.content.Context;
import edu.chalmers.aardvark.AardvarkApp;

public class SettingsCtrl {
    private static SettingsCtrl instance;
    Context context = AardvarkApp.getContext();

    public void getContact() {

    }

    private SettingsCtrl() {
    }

    public static SettingsCtrl getInstance() {
	if (instance == null) {
	    instance = new SettingsCtrl();
	}
	return instance;
    }
}
