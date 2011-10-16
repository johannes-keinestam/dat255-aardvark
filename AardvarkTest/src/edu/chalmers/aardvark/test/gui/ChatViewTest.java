package edu.chalmers.aardvark.test.gui;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.gui.ChatViewActivity;
import edu.chalmers.aardvark.gui.LoginViewActivity;
import edu.chalmers.aardvark.gui.MainViewActivity;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;

public class ChatViewTest extends ActivityInstrumentationTestCase2<LoginViewActivity> {
	private Solo solo;
	private String aardvarkID = "12345";
	private String ContactName = "TestContact";

	public ChatViewTest() {
		super("edu.chalmers.aardvark", LoginViewActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, "testRobotium");
		resetContact();
		addContact();
		solo.clickOnButton("Login");
		solo.waitForDialogToClose(7000);
		receiveMessage();
		solo.clickOnText("null");
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
	public void testReceiveMessage() {
		solo.assertCurrentActivity("chatView", ChatViewActivity.class);
		assertTrue(solo.searchText("testReceiveMessage"));
	}
	public void testSendMessage() {
		solo.assertCurrentActivity("chatView", ChatViewActivity.class);
		solo.enterText(0, "TestSendMessage");
		solo.clickOnButton("Send");
		assertTrue(solo.searchText("TestSendMessage"));
	}
	public void testBlock() {
		solo.pressMenuItem(1);
		solo.enterText(0, "TestBlockMessage");
		solo.clickOnButton("Send");
		assertFalse(solo.searchText("TestBlcokMessage"));
	}
	public void testUnblock() {
		UserCtrl.getInstance().blockUser(aardvarkID);
		solo.pressMenuItem(1);
		solo.enterText(0, "TestBlockMessage");
		solo.clickOnButton("Send");
		assertTrue(solo.searchText("TestBlockMessage"));
	}
	public void testCloseChat(){
		solo.pressMenuItem(0);
		solo.assertCurrentActivity("MainView", MainViewActivity.class);
	}
	public void testAlias(){
		assertTrue(solo.searchText(ContactName));
	}
	public void testAddContact(){
		ContactCtrl.getInstance().removeContact(aardvarkID);
		solo.pressMenuItem(0);
		assertTrue(ContactCtrl.getInstance().isContact(aardvarkID));
		solo.pressMenuItem(0);
		solo.assertCurrentActivity("MainView", MainViewActivity.class);
	}
	private void addContact(){
		ContactCtrl.getInstance().addContact(ContactName, aardvarkID);
	}
	private void resetContact(){
		try {
			UserCtrl.getInstance().unblockUser(aardvarkID);
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
		}
		try{
			ContactCtrl.getInstance().removeContact(aardvarkID);
			
		}
		catch (NullPointerException e) {
			// TODO: handle exception
		}
		
	}
	private void receiveMessage(){
		Message message = new Message();
		message.setBody("testReceiveMessage");
		message.setFrom(aardvarkID);
		ChatCtrl.getInstance().receiveMessage(message);
	}
}
