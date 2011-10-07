package edu.chalmers.aardvarktest.unit.model;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.User;

public class ChatTest {

    Chat chat;
    User user;
    ChatMessage cm;
    
	@Before
	public void setUp() throws Exception {
	    user = DummyUserFactory.getRandomDummyUser();
	    chat = new Chat(user);
	    cm = new ChatMessage(null, null, false, null);
	    
	    chat.addMessage(cm);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAddMessage(){
	    List<ChatMessage> messages = new ArrayList<ChatMessage>();
	    messages.add(cm);
	    
	    chat.addMessage(cm);
	    
	    assertTrue(messages.equals(chat.getMessages()));
	}
	
	@Test
	public void testGetRecipient(){
	    assertTrue(user.equals(chat.getRecipient()));	    
	}

}
