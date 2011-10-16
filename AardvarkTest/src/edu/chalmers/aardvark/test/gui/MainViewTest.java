package edu.chalmers.aardvark.test.gui;

import java.util.ArrayList;

import com.jayway.android.robotium.solo.Solo;

import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.gui.ChatViewActivity;
import edu.chalmers.aardvark.gui.LoginViewActivity;
import edu.chalmers.aardvark.gui.MainViewActivity;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

public class MainViewTest extends ActivityInstrumentationTestCase2<LoginViewActivity> {
	private Solo solo;
	private String aardvarkID = "12345";
	private String ContactName = "TestContact";

	public MainViewTest() {
		super("edu.chalmers.aardvark", LoginViewActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, "testRobotium");
		resetContact();
		addContact();
		solo.clickOnButton("Login");
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

	public void testNewChatWithContact(){
		solo.waitForActivity("MainViewActivity");
		ComBus.notifyListeners(StateChanges.USER_ONLINE.toString(), aardvarkID);
		solo.waitForText(ContactName);
		solo.clickOnText(ContactName);
		solo.assertCurrentActivity("ChatView", ChatViewActivity.class);
	}
	public void testRemoveContact(){
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Remove");
		assertFalse(solo.searchText(ContactName));
	}
	public void testMenuLogout() {
		solo.pressMenuItem(0);
		solo.waitForDialogToClose(5000);
		solo.assertCurrentActivity("test", LoginViewActivity.class, true);
	}
	public void testBlockContact(){
		solo.waitForActivity("MainViewActivity");
		int before = 0;
		ArrayList<ImageView> iv = solo.getCurrentImageViews();
		for (ImageView imageView : iv) {
			before += imageView.getVisibility();
			
		}
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Block");
		int after = 0;
		iv = solo.getCurrentImageViews();
		for (ImageView imageView : iv) {
			System.out.println(imageView.getVisibility());
			after += imageView.getVisibility();
			
		}
		assertFalse(after==before);
		
	}
	public void testUnblockContact(){
		UserCtrl.getInstance().blockUser(aardvarkID);
		solo.waitForActivity("MainViewActivity");
		int before = 0;
		ArrayList<ImageView> iv = solo.getCurrentImageViews();
		for (ImageView imageView : iv) {
			before += imageView.getVisibility();
			
		}
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Unblock");
		solo.waitForDialogToClose(1000);
		int after = 0;
		iv = solo.getCurrentImageViews();
		for (ImageView imageView : iv) {
			after += imageView.getVisibility();
			
		}
		assertFalse(after==before);
		
	}
	public void testSetNickName(){
		solo.waitForActivity("MainViewActivity");
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Rename");
		solo.enterText(0, "TestNickName");
		solo.clickOnButton("Ok");
		assertTrue(solo.searchText("TestNickName"));
		
	}
	public void testSetNickNameCancel(){
		solo.waitForActivity("MainViewActivity");
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Rename");
		solo.enterText(0, "TestNickName");
		solo.clickOnButton("Cancel");
		assertTrue(solo.searchText(ContactName));
		
	}
	public void testAlias(){
		solo.waitForActivity("MainViewActivity");
		assertTrue(solo.searchText("testRobotium"));
	}
	public void testNewChatNoEntry() {
		solo.waitForActivity("MainViewActivity");
		solo.pressMenuItem(0);
		solo.clickOnButton("Start");
		assertFalse(solo.searchText("User name:"));
	}
	public void testNewChatNoUserFound() {
		solo.waitForActivity("MainViewActivity");
		solo.pressMenuItem(0);
		solo.enterText(0, Math.random()*Math.random()*10000000+"");
		solo.clickOnButton("Start");
		assertTrue(solo.searchText("User not found!"));
	}
	public void testNewChatFromPlus(){
	solo.waitForActivity("MainViewActivity");
	solo.clickOnButton(0);
	assertTrue(solo.searchText("User name:"));
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
}
