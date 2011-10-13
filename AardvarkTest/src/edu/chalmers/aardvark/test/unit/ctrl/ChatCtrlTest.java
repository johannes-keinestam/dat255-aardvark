package edu.chalmers.aardvark.test.unit.ctrl;


import static org.junit.Assert.*;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.util.Log;

import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.test.unit.model.DummyUserFactory;
import edu.chalmers.aardvark.util.ServerConnection;

public class ChatCtrlTest {

	ChatCtrl chatCtrl;
	User user;
	
	@Before
	public void setUp() throws Exception {
		chatCtrl = ChatCtrl.getInstance();

		user = DummyUserFactory.getRandomDummyUser();
	}

	@Test
	public void testGetChatInstance(){
		assertTrue(ChatCtrl.getInstance() == chatCtrl);
	}
	
	@Test
	public void testNewGetCloseChat(){
		Chat chat = new Chat(user);
		chatCtrl.newChat(user);
		
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == chat);
		
		chatCtrl.closeChat(chat);
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == null);
	}
	
	@Test
	public void testNewChatGetChats(){
		int numberOfChats = chatCtrl.getChats().size();
		chatCtrl.newChat(user);
		assertTrue(chatCtrl.getChats().size() > numberOfChats);
	}
	
	@Test
	public void  testReceiveSendGetChatMessages(){
		chatCtrl.newChat(user);
		int numberOfMessages = chatCtrl.getChatMessages(user.getAardvarkID()).size();
		
		chatCtrl.sendMessage(chatCtrl.getChat(user.getAardvarkID()), "dummy");
		assertTrue(chatCtrl.getChatMessages(user.getAardvarkID()).size() > numberOfMessages);
		numberOfMessages = chatCtrl.getChatMessages(user.getAardvarkID()).size();
		
		Message messagePacket = new Message("asdf");
		messagePacket.setFrom(user.getAardvarkID());
		messagePacket.setBody("dummy");
	
	    chatCtrl.receiveMessage(messagePacket);
		

		assertTrue(chatCtrl.getChatMessages(user.getAardvarkID()).size() > numberOfMessages);
		
	}
	
	@Test
	public void testCloseChats(){
		Chat chat = new Chat(user);
		chatCtrl.newChat(user);
		
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == chat);
		
		chatCtrl.closeChats();
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == null);
	}
	
	@After
	public void tearDown() throws Exception {
	}	
}
