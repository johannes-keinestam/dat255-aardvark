package edu.chalmers.aardvark.test.unit.ctrl;

import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.test.unit.model.DummyUserFactory;
import junit.framework.TestCase;


public class ContactCtrlTest extends TestCase{
	
	ContactCtrl contactCtrl;
	User user;

	public void setUp() throws Exception {
		contactCtrl = ContactCtrl.getInstance();
		user = DummyUserFactory.getRandomDummyUser();
	}
	
	public void testGetChatInstance(){
		assertTrue(ContactCtrl.getInstance() == contactCtrl);
	}
	
	public void testAddRemoveIsContact(){
		contactCtrl.addContact("asdf", user.getAardvarkID());
		
		contactCtrl.removeContact(user.getAardvarkID());
		assertTrue(!contactCtrl.isContact(user.getAardvarkID()));
	}

	public void testGetContactSetNickname(){
		contactCtrl.addContact("asdf", user.getAardvarkID());
		
		String dummy = "qwer";
		
		contactCtrl.setNickname(user.getAardvarkID(), dummy);
		
		assertTrue(contactCtrl.getContact(user.getAardvarkID()).getNickname() == dummy);
	}
	
	public void testGetRemoveContacts(){
		contactCtrl.addContact("asdf", user.getAardvarkID());
		
		int numberOfContacts = contactCtrl.getContacts().size();
		
		contactCtrl.removeContacts();
		
		assertTrue(contactCtrl.getContacts().size() < numberOfContacts);
	}
	
	public void tearDown() throws Exception {
	}

}
