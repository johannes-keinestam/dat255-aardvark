package edu.chalmers.aardvark.test.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.DataForm.ReportedData;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ImageView;

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

public class MainViewTest extends ActivityInstrumentationTestCase2<LoginViewActivity> {
	private Solo solo;
	private String aardvarkID = "12345";
	private String ContactName = "TestContact";
	HashMap<String, Boolean> report = new HashMap<String, Boolean>();

	public MainViewTest() {
		super("edu.chalmers.aardvark", LoginViewActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, "testRobotium");
		solo.clickOnButton("Login");
		solo.waitForActivity("MainViewActivity");
		
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
	public void testMain(){
		addContact();

		newChatNoUserFound();
		blockContact();
		newChatFromPlus();
		newChatNoEntry();
		unblockContact();
		setNickName();
		setNickNameCancel();
		removeContact();
		newChatWithContact();
		menuLogout();

		printResult();
		
	}
	public void blockContact(){
		solo.waitForActivity("MainViewActivity");
		int before = 0;
		ArrayList<ImageView> iv = solo.getCurrentImageViews();
		for (ImageView imageView : iv) {
			before += imageView.getVisibility();
			
		}
		solo.clickLongOnText(ContactName);
		if(solo.searchText("Block")){
			solo.clickOnText("Block");
		}
		int after = 0;
		iv = solo.getCurrentImageViews();
		for (ImageView imageView : iv) {
			System.out.println(imageView.getVisibility());
			after += imageView.getVisibility();
			
		}

		report.put("blockContact", after!=before);
		resetContact();
		reset();
		
	}
	public void menuLogout() {
		solo.pressMenuItem(2);
		solo.waitForDialogToClose(5000);
		solo.waitForActivity("LoginViewActivity");
		report.put("menuLogout", solo.getCurrentActivity().getLocalClassName().equals("gui.LoginViewActivity"));
	}
	public void newChatFromPlus(){
	solo.waitForActivity("MainViewActivity");
	solo.clickOnButton(0);
	report.put("newChatFromPlus", solo.searchText("User name:"));
	reset();
	}
	public void newChatNoEntry() {
		solo.waitForActivity("MainViewActivity");
		solo.pressMenuItem(0);
		solo.clearEditText(0);
		solo.clickOnButton("Start");
		report.put("newChatNoEntry", solo.searchText("User name:")==false);
		reset();
	}
	public void newChatNoUserFound() {
		solo.waitForActivity("MainViewActivity");
		solo.pressMenuItem(0);
		solo.enterText(0, Math.random()*Math.random()*10000000+"");
		solo.clickOnButton("Start");
		report.put("newChatNoUserFound", solo.searchText("User not found!"));
		reset();
	}
	public void newChatWithContact(){
		solo.waitForActivity("MainViewActivity");
		ComBus.notifyListeners(StateChanges.USER_ONLINE.toString(), aardvarkID);
		solo.waitForText(ContactName);
		solo.clickOnText(ContactName);
		report.put("newChatWithContact",solo.getCurrentActivity().getLocalClassName().equals("gui.ChatViewActivity"));
		reset();
	}
	public void removeContact(){
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Remove");
		report.put("removeContact", solo.searchText(ContactName)==false);
		reset();
		resetContact();
		if(solo.searchText(ContactName)==false){
			addContact();
		}
	}
	public void setNickName(){
		solo.waitForActivity("MainViewActivity");
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Rename");
		solo.enterText(0, "TestNickName");
		solo.clickOnButton("Ok");
		report.put("setNickName", solo.searchText("TestNickName"));
		resetContact();
		reset();
		
	}
	public void setNickNameCancel(){
		solo.waitForActivity("MainViewActivity");
		solo.clickLongOnText(ContactName);
		solo.clickOnText("Rename");
		solo.enterText(0, "TestNickName");
		solo.clickOnButton("Cancel");
		report.put("setNickNameCancel", solo.searchText(ContactName));
		resetContact();
		reset();
		
	}
	public void unblockContact(){
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
		report.put("unblockContact", after!=before);
		resetContact();
		reset();
		
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
			ContactCtrl.getInstance().setNickname(aardvarkID, ContactName);
			
		}
		catch (NullPointerException e) {
			// TODO: handle exception
		}
		
	}

	private void reset(){
		for (int i = 0; i < 4; i++) {
			solo.goBack();
		}
		
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
