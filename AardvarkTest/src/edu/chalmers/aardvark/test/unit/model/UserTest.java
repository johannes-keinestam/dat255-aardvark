package edu.chalmers.aardvark.test.unit.model;

import edu.chalmers.aardvark.model.User;
import junit.framework.TestCase;

public class UserTest extends TestCase {
	User user;
	String aardvarkID;
	String alias;
	
	protected void setUp() throws Exception {
		super.setUp();
		aardvarkID = "AardvarkID "+Math.random();
		alias = "Alias "+Math.random();
		user = new User(alias, aardvarkID);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAardvarkID() {
		assertTrue(user.getAardvarkID().equals(aardvarkID));
	}

	public void testGetAlias() {
		assertTrue(user.getAlias().equals(alias));
	}

	public void testSetAlias() {
		alias = "New Alias "+Math.random();
		user.setAlias(alias);
		
		assertTrue(user.getAlias().equals(alias));
	}

}
