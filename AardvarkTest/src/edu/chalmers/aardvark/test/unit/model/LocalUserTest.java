package edu.chalmers.aardvark.test.unit.model;

import edu.chalmers.aardvark.model.LocalUser;
import junit.framework.TestCase;

public class LocalUserTest extends TestCase {
	String aardvarkID;
	String password;
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateUser() {
		aardvarkID = "AardvarkID "+Math.random(); 
		password = "Password "+Math.random(); 
		LocalUser.createUser(aardvarkID, password);		
		
		assertFalse(LocalUser.getLocalUser() == null);
	}

	public void testSetAlias() {
		aardvarkID = "AardvarkID "+Math.random(); 
		password = "Password "+Math.random();
		String alias = "Alias "+Math.random(); 
		LocalUser.createUser(aardvarkID, password);
		LocalUser.setAlias(alias);
		
		assertTrue(LocalUser.getLocalUser().getAlias().equals(alias));
	}

	public void testGetLocalUser() {
		aardvarkID = "AardvarkID "+Math.random(); 
		password = "Password "+Math.random(); 
		LocalUser.createUser(aardvarkID, password);		
		
		assertTrue(LocalUser.getLocalUser().getAardvarkID().equals(aardvarkID));
	}

	public void testGetPassword() {
		aardvarkID = "AardvarkID "+Math.random(); 
		password = "Password "+Math.random(); 
		LocalUser.createUser(aardvarkID, password);		
		
		assertTrue(LocalUser.getPassword().equals(password));
	}

	public void testRemoveUser() {
		aardvarkID = "AardvarkID "+Math.random(); 
		password = "Password "+Math.random(); 
		LocalUser.createUser(aardvarkID, password);		
		LocalUser.removeUser();
		
		assertTrue(LocalUser.getLocalUser() == null);
	}

}
