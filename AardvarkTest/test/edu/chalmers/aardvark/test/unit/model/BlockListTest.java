package edu.chalmers.aardvark.test.unit.model;

import edu.chalmers.aardvark.model.BlockList;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.User;
import junit.framework.TestCase;

public class BlockListTest extends TestCase {
	BlockList bl;
	
	protected void setUp() throws Exception {
		super.setUp();
		bl = new BlockList();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddUser() {
		User user = DummyUserFactory.getRandomDummyUser();
		bl.addUser(user);
		
		assertFalse(bl.findUser(user.getAardvarkID()) == null);
	}

	public void testRemoveUser() {
		bl.getBlockedUsers().clear();
		User user = DummyUserFactory.getRandomDummyUser();
		bl.addUser(user);
		
		bl.removeUser(user);
		assertTrue(bl.findUser(user.getAardvarkID()) == null);
	}

	public void testFindUser() {
		bl.getBlockedUsers().clear();
		User user = DummyUserFactory.getRandomDummyUser();
		bl.addUser(user);
		
		assertTrue(bl.findUser(user.getAardvarkID()) == user);
	}

	public void testGetBlockedUsers() {
		int blockedBefore = bl.getBlockedUsers().size();
		
		User user1 = DummyUserFactory.getRandomDummyUser();
		User user2 = DummyUserFactory.getRandomDummyUser();

	    bl.addUser(user1);
	    bl.addUser(user2);
	    
	    int blockedAfter = bl.getBlockedUsers().size();
	    
	    assertTrue(blockedBefore+2 == blockedAfter);
	}

}
