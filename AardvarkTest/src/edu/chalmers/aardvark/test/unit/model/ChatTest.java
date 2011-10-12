package edu.chalmers.aardvark.test.unit.model;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.User;

public class ChatTest {

    Chat chat;
    User user;
    ChatMessage cm;
    
	public void setUp() throws Exception {
	    user = DummyUserFactory.getRandomDummyUser();
	    chat = new Chat(user);
	    cm = new ChatMessage(null, null, false, null);
	    
	    chat.addMessage(cm);
	}

	public void tearDown() throws Exception {
	}
	
	public void testAddMessage(){
	    List<ChatMessage> messages = new ArrayList<ChatMessage>();
	    messages.add(cm);
	    
	    chat.addMessage(cm);
	    
	    assertTrue(messages.equals(chat.getMessages()));
	}
	
	public void testGetRecipient(){
	    assertTrue(user.equals(chat.getRecipient()));	    
	}
	
	public void testGetMessages(){
		
		int msgBefore = chat.getMessages().size();
		
	    ChatMessage cm1 = new ChatMessage("msg 1", null, false, null);
	    ChatMessage cm2 = new ChatMessage("msg 2", null, false, null);
	    
	    chat.addMessage(cm1);
	    chat.addMessage(cm2);
	    
	    int msgAfter = chat.getMessages().size();
	    
	    assertTrue(msgBefore+2 == msgAfter);    
	}

}
