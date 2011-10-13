package edu.chalmers.aardvark.test.unit.util;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.EventListener;
import edu.chalmers.aardvark.util.StateChanges;
import junit.framework.TestCase;

/**
 * 
 * Test class for ComBus class. Ultimately also tests the EventListener 
 * interface and the StateChanges enum.
 *
 */
public class ComBusTest extends TestCase implements EventListener {

	boolean notified = false;
	
	protected void setUp() throws Exception {
		super.setUp();
		ComBus.subscribe(this);
		notified = false;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testUnsubscribe() {
		StateChanges sc = StateChanges.STARTING_UP;
		ComBus.unsubscribe(this);
		ComBus.notifyListeners(sc.toString(), null);
		
		assertFalse(notified);
	}

	public void testNotifyListeners() {
		StateChanges sc = StateChanges.STARTING_UP;
		ComBus.notifyListeners(sc.toString(), null);
		
		assertTrue(notified);
	}

	public void notifyEvent(String stateChange, Object object) {
		if (StateChanges.valueOf(stateChange) == StateChanges.STARTING_UP) {
			notified = true;
		}
	}

}
