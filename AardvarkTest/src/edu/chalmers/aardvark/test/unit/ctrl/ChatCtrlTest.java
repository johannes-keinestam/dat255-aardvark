package edu.chalmers.aardvark.test.unit.ctrl;


import junit.framework.TestCase;
import org.jivesoftware.smack.packet.Message;
import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.test.unit.model.DummyUserFactory;

public class ChatCtrlTest extends TestCase{

	ChatCtrl chatCtrl;
	User user;
	
	public void setUp() throws Exception {
		chatCtrl = ChatCtrl.getInstance();

		user = DummyUserFactory.getRandomDummyUser();
	}

	public void testGetChatInstance(){
		assertTrue(ChatCtrl.getInstance() == chatCtrl);
	}
	
	public void testNewGetCloseChat(){
		Chat chat = new Chat(user);
		chatCtrl.newChat(user);
		
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == chat);
		
		chatCtrl.closeChat(chat);
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == null);
	}
	
	public void testNewChatGetChats(){
		int numberOfChats = chatCtrl.getChats().size();
		chatCtrl.newChat(user);
		assertTrue(chatCtrl.getChats().size() > numberOfChats);
	}
	
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
	
	public void testCloseChats(){
		Chat chat = new Chat(user);
		chatCtrl.newChat(user);
		
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == chat);
		
		chatCtrl.closeChats();
		assertTrue(chatCtrl.getChat(user.getAardvarkID()) == null);
	}
	
	public void tearDown() throws Exception {
	}	
}
