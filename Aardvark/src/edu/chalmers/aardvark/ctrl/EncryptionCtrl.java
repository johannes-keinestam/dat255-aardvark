package edu.chalmers.aardvark.ctrl;

public class EncryptionCtrl {
    private static EncryptionCtrl instance;

    private EncryptionCtrl() {
    }

    public static EncryptionCtrl getInstance() {
	if (instance == null) {
	    instance = new EncryptionCtrl();
	}
	return instance;
    }
}
