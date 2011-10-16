package edu.chalmers.aardvark.test.gui;

import com.jayway.android.robotium.solo.Solo;

import edu.chalmers.aardvark.gui.ChatViewActivity;
import edu.chalmers.aardvark.gui.LoginViewActivity;
import edu.chalmers.aardvark.gui.MainViewActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.EditText;

public class LoginViewTest extends ActivityInstrumentationTestCase2<LoginViewActivity> {
	private Solo solo;

	public LoginViewTest() {
		super("edu.chalmers.aardvark", LoginViewActivity.class);
	}
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
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
	public void testLogin() {
		solo.enterText(0, "testRobotium");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("sdsda", MainViewActivity.class, true);
	}
}
