package edu.chalmers.aardvark.test.unit.ctrl;

import junit.framework.TestCase;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.test.unit.model.DummyUserFactory;

public class UserCtrlTest extends TestCase{

	private UserCtrl userCtrl;
	private User user;
	
	public void setUp() throws Exception {
		userCtrl = UserCtrl.getInstance();
		
		user = DummyUserFactory.getRandomDummyUser();
	}
	
	public void testGetuserInstance(){
		assertTrue(UserCtrl.getInstance() == userCtrl);
	}
	
	public void testBlockUser() {
		userCtrl.blockUser(user);
		
		assertTrue(userCtrl.isUserBlocked(user.getAardvarkID()));
		
		userCtrl.unblockUser(user);
	}
	
	public void testBlocAardvarkID() {
		userCtrl.blockUser(user.getAardvarkID());
		
		assertTrue(userCtrl.isUserBlocked(user.getAardvarkID()));
		
		userCtrl.unblockUser(user.getAardvarkID());
	}
	
	public void testUnblockUser() {
		userCtrl.blockUser(user);
		userCtrl.unblockUser(user);
		
		assertFalse(userCtrl.isUserBlocked(user.getAardvarkID()));
	}
	
	public void testUnblockAardvarkID() {
		userCtrl.blockUser(user.getAardvarkID());
		userCtrl.unblockUser(user.getAardvarkID());
		
		assertFalse(userCtrl.isUserBlocked(user.getAardvarkID()));
	}

	public void tearDown() throws Exception {
	}

}
