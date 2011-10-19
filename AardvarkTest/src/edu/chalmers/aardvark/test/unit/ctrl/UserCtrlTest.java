package edu.chalmers.aardvark.test.unit.ctrl;

import junit.framework.TestCase;
import edu.chalmers.aardvark.ctrl.String;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.User;

public class UserCtrlTest extends TestCase{

	private UserCtrl userCtrl;
	User user;
	User userB;
	
	public void setUp() throws Exception {
		userCtrl = UserCtrl.getInstance();
		
		user = DummyUserFactory.getRandomDummyUser();
		userB = DummyUserFactory.getRandomDummyUser();
	}
	
	public void testGetuserInstance(){
		assertTrue(UserCtrl.getInstance() == userCtrl);
	}
	
	public void testBlockUser() {
		blockUser(user);
		
		assertTrue(isUserBlocked(user.getAardvarkID()));
		
		unblockUser(user);
	}
	
	public void testBlocAardvarkID() {
		blockUser(user.getAardvarkID());
		
		assertTrue(isUserBlocked(user.getAardvarkID()));
		
		unblockUser(user.getAardvarkID());
	}
	
	public void testUnblockUser() {
		blockUser(user);
		unblockUser(user);
		
		assertFalse(isUserBlocked(user.getAardvarkID()));
	}
	
	public void testUnblockAardvarkID() {
		blockUser(user.getAardvarkID());
		unblockUser(user.getAardvarkID());
		
		assertFalse(isUserBlocked(user.getAardvarkID()));
	}

	public void tearDown() throws Exception {
	}

}
