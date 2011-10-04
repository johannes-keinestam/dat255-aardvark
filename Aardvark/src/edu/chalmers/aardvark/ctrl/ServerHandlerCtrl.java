package edu.chalmers.aardvark.ctrl;

import edu.chalmers.aardvark.model.User;

public class ServerHandlerCtrl {
    
    private static ServerHandlerCtrl instance;

    private ServerHandlerCtrl() {
    }

    public static ServerHandlerCtrl getInstance() {
	if (instance == null) {
	    instance = new ServerHandlerCtrl();
	}
	return instance;
    }
    
    public boolean isOnline(User user){
	return true;
    }
    
    public void newChat(){};
}
