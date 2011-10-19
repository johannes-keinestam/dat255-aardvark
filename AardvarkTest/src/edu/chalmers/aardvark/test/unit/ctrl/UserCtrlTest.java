package edu.chalmers.aardvark.test.unit.ctrl;

import junit.framework.TestCase;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.User;

public class UserCtrlTest extends TestCase{

	private UserCtrl userCtrl;
	User userA;
	User userB;
	
	public void setUp() throws Exception {
		userCtrl = UserCtrl.getInstance();
		
		userA = DummyUserFactory.getRandomDummyUser();
		userB = DummyUserFactory.getRandomDummyUser();
	}
	
	public void testGetuserInstance(){
		assertTrue(UserCtrl.getInstance() == userCtrl);
	}
	
	public void testBlockUser(User user) {
		
	}

	public void tearDown() throws Exception {
	}

}
