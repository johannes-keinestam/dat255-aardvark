package edu.chalmers.aardvark.test.unit.model;

import edu.chalmers.aardvark.model.Contact;
import junit.framework.TestCase;

public class ContactTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetNickname() {
		String nickname = "Nickname "+Math.random();
		Contact contact = new Contact(nickname, "AardvarkID "+Math.random());
		
		assertTrue(contact.getNickname().equals(nickname));
	}

	public void testRename() {
		String newNickname = "New nickname "+Math.random();
		Contact contact = DummyUserFactory.getRandomDummyContact();
		contact.rename(newNickname);
		
		assertTrue(contact.getNickname().equals(newNickname));
	}

}
