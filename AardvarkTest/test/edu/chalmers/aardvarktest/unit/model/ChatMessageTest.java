package edu.chalmers.aardvarktest.unit.model;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.text.format.Time;

import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.User;

public class ChatMessageTest {

    ChatMessage cm;
    String message;
    User user;
    boolean isRemote;
    Time timeStamp;

    @Before
    public void setUp() throws Exception {
	message = "Message "+((Math.random()*100)+(Math.random()*10));
	user = DummyUserFactory.getRandomDummyUser();
	isRemote = false;
	timeStamp = new Time(Time.getCurrentTimezone());
	timeStamp.setToNow();

	cm = new ChatMessage(message, user, isRemote, timeStamp);
    }


    @Test
    public void testGetMessage() {
	assertTrue(cm.getMessage().equals(message));
    }
    
    @Test
    public void testGetUser() {
	assertTrue(cm.getUser().equals(user));
    }

    @Test
    public void testIsRemote() {
	assertTrue(cm.isRemote() == isRemote);
    }

    @Test
    public void testGetTimeStamp() {
	assertTrue(cm.getTimeStamp().equals(timeStamp));
    }

    @After
    public void tearDown() throws Exception {
    }

}
