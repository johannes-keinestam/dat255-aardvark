package edu.chalmers.aardvark.ctrl;

public class SettingsCtrl {
    private static SettingsCtrl instance;

    private SettingsCtrl() {
    }

    public static SettingsCtrl getInstance() {
	if (instance == null) {
	    instance = new SettingsCtrl();
	}
	return instance;
    }
}
