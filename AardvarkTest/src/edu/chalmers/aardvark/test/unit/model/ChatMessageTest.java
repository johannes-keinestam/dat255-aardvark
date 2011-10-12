package edu.chalmers.aardvark.test.unit.model;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import android.text.format.Time;

import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.User;

public class ChatMessageTest extends TestCase {

    ChatMessage cm;
    String message;
    User user;
    boolean isRemote;
    Time timeStamp;

    protected void setUp() throws Exception {
    	super.setUp();
		message = "Message "+((Math.random()*100)+(Math.random()*10));
		user = DummyUserFactory.getRandomDummyUser();
		isRemote = false;
		timeStamp = new Time(Time.getCurrentTimezone());
		timeStamp.setToNow();
	
		cm = new ChatMessage(message, user, isRemote, timeStamp);
    }


    public void testGetMessage() {
	assertTrue(cm.getMessage().equals(message));
    }
    
    public void testGetUser() {
	assertTrue(cm.getUser().equals(user));
    }

    public void testIsRemote() {
	assertTrue(cm.isRemote() == isRemote);
    }

    public void testGetTimeStamp() {
	assertTrue(cm.getTimeStamp().equals(timeStamp));
    }

    protected void tearDown() throws Exception {
    	super.tearDown();
    }

}
