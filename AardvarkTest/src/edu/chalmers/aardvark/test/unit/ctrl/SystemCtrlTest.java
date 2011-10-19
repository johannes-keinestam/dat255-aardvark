package edu.chalmers.aardvark.test.unit.ctrl;

import junit.framework.TestCase;
import edu.chalmers.aardvark.ctrl.SystemCtrl;

public class SystemCtrlTest extends TestCase {

	private SystemCtrl systemCtrl;

	public void setUp() throws Exception {
		systemCtrl = SystemCtrl.getInstance();
	}
	
	public void testGetsystemInstance(){
		assertTrue(SystemCtrl.getCtrl() == systemCtrl);
	}
	
	public void tearDown() throws Exception {
	}

}
