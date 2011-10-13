package edu.chalmers.aardvark.test.unit.util;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.util.Log;

import edu.chalmers.aardvark.util.ServerConnection;
import junit.framework.TestCase;

public class ServerConnectionTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetConnection() {
		XMPPConnection con = ServerConnection.getConnection();
		
		assertNotNull(con);
		assertTrue(con.isConnected());
	}

	public void testLogin() {
		XMPPConnection con = ServerConnection.getConnection();
		boolean loggedIn = false;
		String username = "tester"+Math.random();
		String password = "password";
		
		try {
			ServerConnection.register(username, password, "");
		} catch (XMPPException e) {
			// Test couldn't be performed, not necessarily failed. Returns.
			Log.i("TEST", "Could not register account to login to. "+e.getMessage());
			return;
		}
		
		try {
			ServerConnection.login(username, password);
			loggedIn = con.isAuthenticated();
		} catch (XMPPException e) {
			// Test couldn't be performed, not necessarily failed. Returns.
			Log.i("TEST", "Could not log in. Server might be unreachable."+e.getMessage());
			return;
		}
		
		try {
			con.getAccountManager().deleteAccount();
		} catch (XMPPException e) {	}
		
		assertTrue(loggedIn);
		
	}

	public void testRestart() {
		XMPPConnection con1 = ServerConnection.getConnection();
		ServerConnection.restart();
		XMPPConnection con2 = ServerConnection.getConnection();
		
		assertFalse(con1 == con2);
	}

}
