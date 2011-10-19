package edu.chalmers.aardvark.test.unit.ctrl;

import junit.framework.TestCase;
import edu.chalmers.aardvark.ctrl.SettingsCtrl;

public class SettingsCtrlTest {

	private SettingsCtrl settingsCtrl;

	public void setUp() throws Exception {
		settingsCtrl = SettingsCtrl.getInstance();
	}
	
	public void testGetSettingsInstance(){
		assertTrue(SettingsCtrl.getInstance() == settingsCtrl);
	}

}
