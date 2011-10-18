package edu.chalmers.aardvark.test.gui;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.gui.LoginViewActivity;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

public class ChatViewTest extends ActivityInstrumentationTestCase2<LoginViewActivity> {
	private Solo solo;
	private String aardvarkID = "chatViewTest"+Math.random();
	HashMap<String, Boolean> report = new HashMap<String, Boolean>();

	public ChatViewTest() {
		super("edu.chalmers.aardvark", LoginViewActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, "testRobotium");
		solo.clickOnButton("Login");
		solo.waitForActivity("MainViewActivity");
		ChatCtrl.getInstance().closeChats();
		receiveMessage();
		
	}
	
	public void tearDown() throws Exception {
		try {
			solo.finalize(); 	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}
	public void testChat(){
		acceptChatTest();
		receiveMessageTest();
		sendMessageTest();
		addContactTest();
		blockTest();
		unblockTest();
		closeChatTest();
		logout();

		printResult();
		
	}

	private void logout() {
		for (int i = 0; i < 4; i++) {
			solo.goBack();
		}
		ComBus.notifyListeners(StateChanges.USER_OFFLINE.toString(), aardvarkID);
		try {
			ContactCtrl.getInstance().removeContact(aardvarkID);
		} catch (NullPointerException e) {
			
		}
		solo.pressMenuItem(2);
		solo.waitForActivity("LoginViewActivity");
	}

	private void closeChatTest() {
		solo.pressMenuItem(0);
		report.put("closeChatTest", solo.getCurrentActivity().getLocalClassName().equals("gui.MainViewActivity"));
	
	}

	private void unblockTest() {
		solo.pressMenuItem(1);
		solo.enterText(0, "TestunblockMessage");
		solo.clickOnButton("Send");
		report.put("unblockTest", solo.searchText("TestunblockMessage"));
	}

	private void blockTest() {
		solo.pressMenuItem(1);
		solo.enterText(0, "TestBlockMessage");
		solo.clickOnButton("Send");
		report.put("blockTest", solo.searchText("TestBlcokMessage")==false);	
		
	}

	private void addContactTest() {
		solo.pressMenuItem(1);
		solo.enterText(0, "testAddContact");
		solo.clickOnButton("Ok");
		report.put("addContactTest", solo.searchText("Contact added"));	
	}

	private void sendMessageTest() {
		solo.enterText(0, "testSendMessage");
		solo.clickOnButton("Send");
		report.put("sendMessageTest", solo.searchText("testSendMessage"));	
		
	}

	private void receiveMessageTest() {
		report.put("receiveMessageTest", solo.searchText("testReceiveMessage"));		
	}

	private void acceptChatTest() {
		solo.clickOnButton("Yes");
		report.put("acceptChatTest", solo.getCurrentActivity().getLocalClassName().equals("gui.ChatViewActivity"));
		
	}


	private void receiveMessage(){
		Message message = new Message();
		message.setBody("testReceiveMessage");
		message.setFrom(aardvarkID);
		ChatCtrl.getInstance().receiveMessage(message);
	}
		
	private void printResult(){
		String failed = "";
		for (Map.Entry<String, Boolean> entry : report.entrySet()) {
        	if(!entry.getValue()){
        		failed =failed+" "+entry.getKey()+" ";
        		System.out.println(failed);
        	}
        }
		if(failed.length()>0){
    		fail(failed);
    	}
	}
}
