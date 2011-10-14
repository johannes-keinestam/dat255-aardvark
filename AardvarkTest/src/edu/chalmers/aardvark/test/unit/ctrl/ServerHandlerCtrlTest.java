package edu.chalmers.aardvark.test.unit.ctrl;


import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import junit.framework.TestCase;


public class ServerHandlerCtrlTest extends TestCase{

	ServerHandlerCtrl serverCtrl;
	
	public void setUp() throws Exception {
		serverCtrl = ServerHandlerCtrl.getInstance();
	}
	
	public void testGetServerHandlerInstance(){
		assertTrue(ServerHandlerCtrl.getInstance() == serverCtrl);
	}
	
	

}
