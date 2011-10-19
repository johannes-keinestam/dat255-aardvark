package edu.chalmers.aardvark.test.unit.ctrl;


import java.util.Map;

import android.content.SharedPreferences;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.ctrl.SettingsCtrl;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.test.unit.model.DummyUserFactory;
import junit.framework.TestCase;


public class SettingsCtrlTest extends TestCase {

	SettingsCtrl settingsCtrl;

	public void setUp() throws Exception {
		settingsCtrl = SettingsCtrl.getInstance();
	}

	public void tearDown(){
	}
	
	public void testSettingsCtrlInstance(){
		assertTrue(SettingsCtrl.getInstance() == settingsCtrl);
	}
	
	public void testSaveRenameRemoveContact(){
		boolean stageOne = false;
		boolean stageTwo = false;
		boolean stageThree = true;
		Contact contact = DummyUserFactory.getRandomDummyContact();
		
		settingsCtrl.saveContact(contact);
		
		SharedPreferences savedContacts = AardvarkApp.getContext().getSharedPreferences("contacts",
				0);
		for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
			if(entry.getKey().toString().equals(contact.getAardvarkID())){
				stageOne = true;
			}
		}
		
		settingsCtrl.renameContact(contact, "NewGloriousNickname");
		
		savedContacts = AardvarkApp.getContext().getSharedPreferences("contacts",
				0);
		for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
			if(entry.getKey().toString().equals(contact.getAardvarkID())){
				if(entry.getValue().toString().equals("NewGloriousNickname")){
					stageTwo = true;
				}
			}
		}
		
		settingsCtrl.deleteContact(contact);
			
		for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
			if(entry.getKey().toString().equals(contact.getAardvarkID())){
				stageThree = false;
			}
		}
		
		assertTrue(stageOne && stageTwo && stageThree);
		
	}
	
	public void testSaveRemoveBlockedUser(){
		boolean stageOne = false;
		boolean stageTwo = true;
		User user = DummyUserFactory.getRandomDummyUser();
		
		settingsCtrl.saveBlockedUser(user);
		
		SharedPreferences savedContacts = AardvarkApp.getContext().getSharedPreferences("blocklist",
				0);
		for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
			if(entry.getKey().toString().equals(user.getAardvarkID())){
				stageOne = true;
			}
		}
		
		
		settingsCtrl.deleteBlockedUser(user);
			
		savedContacts = AardvarkApp.getContext().getSharedPreferences("blocklist",
				0);
		for (Map.Entry<String, ?> entry : savedContacts.getAll().entrySet()) {
			if(entry.getKey().toString().equals(user.getAardvarkID())){
				stageTwo= false;
			}
		}
		
		assertTrue(stageOne && stageTwo);
	}
}