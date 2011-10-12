package edu.chalmers.aardvark.test.unit.model;

import static org.junit.Assert.assertTrue;
import edu.chalmers.aardvark.model.ActiveChatContainer;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.User;
import junit.framework.TestCase;

public class ActiveChatContainerTest extends TestCase {

	ActiveChatContainer act;

	protected void setUp() throws Exception {
		super.setUp();
		act = new ActiveChatContainer();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddChat() {
		Chat chat = new Chat(DummyUserFactory.getRandomDummyUser());
			    
		act.addChat(chat);
			    
		assertTrue(act.findChatByID(chat.getRecipient().getAardvarkID()) != null);
	}

	public void testRemoveChat() {
		Chat chat = new Chat(DummyUserFactory.getRandomDummyUser());
	    
	    act.addChat(chat);
	    act.removeChat(chat);
	    
	    assertTrue(act.findChatByID(chat.getRecipient().getAardvarkID()) == null);
	}

	public void testFindChatByID() {
		User dummyUser = DummyUserFactory.getRandomDummyUser();
		Chat addedChat = new Chat(dummyUser);
		
		act.addChat(addedChat);
		Chat foundChat = act.findChatByID(dummyUser.getAardvarkID());
		act.getChats().clear();
		
		assertTrue(foundChat == addedChat);
	}

	public void testGetChats() {
		int chatsBefore = act.getChats().size();
		
	    Chat chat1 = new Chat(DummyUserFactory.getRandomDummyUser());
	    Chat chat2 = new Chat(DummyUserFactory.getRandomDummyUser());

	    act.addChat(chat1);
	    act.addChat(chat2);
	    
	    int chatsAfter = act.getChats().size();
	    
	    assertTrue(chatsBefore+2 == chatsAfter);
	}

}
