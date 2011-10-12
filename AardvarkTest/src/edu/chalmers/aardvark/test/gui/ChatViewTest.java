package edu.chalmers.aardvark.test.gui;

import com.jayway.android.robotium.solo.Solo;

import edu.chalmers.aardvark.gui.ChatViewActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;

public class ChatViewTest extends ActivityInstrumentationTestCase2<ChatViewActivity> {
	private Solo solo;

	public ChatViewTest() {
		super("edu.chalmers.aardvark", ChatViewActivity.class);
	}
	
	public void setUp() throws Exception {
		//solo = new Solo(getInstrumentation(), getActivity());
	}

	/*@Smoke
	public void testSendMessage() throws Exception {
		fail()
	}*/
	
	public void tearDown() throws Exception {
		try {
			solo.finalize(); 	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	} 
}
