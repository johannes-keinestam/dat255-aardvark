package edu.chalmers.aardvark.test.unit.ctrl;


import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;

import android.util.Log;

import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.EventListener;
import edu.chalmers.aardvark.util.StateChanges;
import junit.framework.TestCase;


public class ServerHandlerCtrlTest extends TestCase implements EventListener{

	ServerHandlerCtrl serverCtrl;
	String aardvarkID;
	String sender;
	String alias;
	String packetID;
	boolean loggedIn;
	XMPPConnection connection;
	User secondUser;
	String password;
	
	
	private void startConnection() throws XMPPException {
		String server_addr = "95.80.11.191";
		int server_port = 5222;

		// connect to server
		ConnectionConfiguration config = new ConnectionConfiguration(server_addr, server_port);
		config.setSASLAuthenticationEnabled(false);
		config.setSelfSignedCertificateEnabled(false);
		config.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPConnection(config);
		connection.connect();
	}
	
	protected void setUp() throws Exception{
		startConnection();
		
		secondUser = new User("ServerHandlerTester","serverhandlertester");
		password = "asdf";
		
		serverCtrl = ServerHandlerCtrl.getInstance();
		
		aardvarkID = "TestUser"+((int)(Math.random()*100)+(int)(Math.random()*10));
		alias = "ID"+((int)(Math.random()*100)+(int)(Math.random()*10));
		String password = "test";

		LocalUser.createUser(aardvarkID, password);
		
		loggedIn = false;
		
		ComBus.subscribe(this);
	}

	protected void tearDown(){
		connection = null;
	}
	
	public void testServerHandlerInstance(){
		assertTrue(ServerHandlerCtrl.getInstance() == serverCtrl);
	}
	
	/*
	public void testLogInWithAliasIsOnline(){
		try {
			connection.login(secondUser.getAardvarkID(), password);
		} catch (Exception e1) {
				Log.i("TEST", "Couldn't connect to server... try later");
		}
		
		serverCtrl.logInWithAlias(alias);
		
		int counter = 0;
		while(!loggedIn){
			try {
                Thread.sleep(1000);
	        } catch (InterruptedException e) {}
	        counter++;
	        if (counter >= 15) {
	                break;
	        }
		}
		
		assertTrue(serverCtrl.isOnline(secondUser));

		serverCtrl.logOut();
	}
	*/
	
	public void testLogOutIsLoggedIn(){
		serverCtrl.logInWithAlias(alias);
		
		int counter = 0;
		while(!loggedIn){
			try {
                Thread.sleep(1000);
	        } catch (InterruptedException e) {}
	        counter++;
	        if (counter >= 15) {
	                break;
	        }
		}
		
		assertTrue(serverCtrl.isLoggedIn());
		
		serverCtrl.logOut();
		
		while(loggedIn){
			try {
                Thread.sleep(1000);
	        } catch (InterruptedException e) {}
	        counter++;
	        if (counter >= 15) {
	                break;
	        }
		}
		
		assertTrue(!serverCtrl.isLoggedIn());
	}
	
	public void testGetAliasAardvarkID(){
		serverCtrl.logInWithAlias(alias);
		
		try {
			connection.login(secondUser.getAardvarkID(), password);
		} catch (Exception e1) {
				Log.i("TEST", "Couldn't connect to server... try later");
		}
		
		int counter = 0;
		while(!loggedIn){
			try {
                Thread.sleep(1000);
	        } catch (InterruptedException e) {}
	        counter++;
	        if (counter >= 15) {
	                break;
	        }
		}
		assertTrue(serverCtrl.getAardvarkID(secondUser.getAlias()).equals(secondUser.getAardvarkID()));
		assertTrue(serverCtrl.getAlias(secondUser.getAardvarkID()).equals(secondUser.getAlias()));		
		
		serverCtrl.logOut();
	}
	
	public void testGetRegisteredUsers(){
		serverCtrl.logInWithAlias(alias);
		
		int counter = 0;
		while(!loggedIn){
			try {
                Thread.sleep(1000);
	        } catch (InterruptedException e) {}
	        counter++;
	        if (counter >= 15) {
	                break;
	        }
		}
				
		assertTrue(serverCtrl.getRegisteredUsers().size() > 0);

		serverCtrl.logOut();
	}

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.LOGGED_IN.toString())) {
			loggedIn = true;
		}else if (stateChange.equals(StateChanges.LOGGED_OUT.toString())){
			loggedIn = false;
		}	
	}
}
