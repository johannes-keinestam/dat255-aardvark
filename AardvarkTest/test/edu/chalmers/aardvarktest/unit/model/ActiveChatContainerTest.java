package edu.chalmers.aardvarktest.unit.model;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.chalmers.aardvark.model.ActiveChatContainer;
import edu.chalmers.aardvark.model.Chat;

public class ActiveChatContainerTest {

    	ActiveChatContainer act;
    
	@Before
	public void setUp() throws Exception {
	    act = new ActiveChatContainer();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAddChat(){
	    Chat chat = new Chat(DummyUserFactory.getRandomDummyUser());
	    
	    act.addChat(chat);
	    
	    assertTrue(act.findChatByID(chat.getRecipient().getAardvarkID()) != null);
	}
	
	public void testRemoveChat(){
	    Chat chat = new Chat(DummyUserFactory.getRandomDummyUser());
	    
	    act.addChat(chat);
	    act.removeChat(chat);
	    
	    assertTrue(act.findChatByID(chat.getRecipient().getAardvarkID()) == null);
	}

}
