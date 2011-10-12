package edu.chalmers.aardvark.test.unit.model;

import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.ContactsContainer;
import junit.framework.TestCase;

public class ContactsContainerTest extends TestCase {

	ContactsContainer cc;
	
	protected void setUp() throws Exception {
		super.setUp();
		cc = new ContactsContainer();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddContact() {
		cc.getList().clear();
		Contact c = DummyUserFactory.getRandomDummyContact();
		cc.addContact(c);
		
		assertFalse(cc.findContact(c.getNickname()) == null);
	}

	public void testRemoveContact() {
		cc.getList().clear();
		Contact c = DummyUserFactory.getRandomDummyContact();
		cc.addContact(c);
		cc.removeContact(c);
		
		assertTrue(cc.findContact(c.getNickname()) == null);
	}

	public void testFindContact() {
		cc.getList().clear();
		Contact c = DummyUserFactory.getRandomDummyContact();
		cc.addContact(c);
		Contact c2 = cc.findContact(c.getNickname());
		
		assertTrue(c2 == c);
	}

	public void testFindContactByID() {
		cc.getList().clear();
		Contact c = DummyUserFactory.getRandomDummyContact();
		cc.addContact(c);
		Contact c2 = cc.findContactByID(c.getAardvarkID());
		
		assertTrue(c2 == c);
	}

	public void testGetList() {
		int contactsBefore = cc.getList().size();
		
	    Contact contact1 = DummyUserFactory.getRandomDummyContact();
	    Contact contact2 = DummyUserFactory.getRandomDummyContact();

	    cc.addContact(contact1);
	    cc.addContact(contact2);
	    
	    int contactsAfter = cc.getList().size();
	    
	    assertTrue(contactsBefore+2 == contactsAfter);

	}

}
