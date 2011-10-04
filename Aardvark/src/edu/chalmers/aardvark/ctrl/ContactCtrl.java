package edu.chalmers.aardvark.ctrl;


public class ContactCtrl {

    private static ContactCtrl instance;

    private ContactCtrl() {
    }

    public static ContactCtrl getInstance() {
	if (instance == null) {
	    instance = new ContactCtrl();
	}
	return instance;
    }    
    
    public void setAlias/*Nickname?*/(){}
    
    public void openChat(){}
    

    
}
