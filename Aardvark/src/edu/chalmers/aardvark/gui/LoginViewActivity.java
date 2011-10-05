package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.ctrl.SystemCtrl;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.EventListener;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginViewActivity extends Activity implements edu.chalmers.aardvark.util.EventListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	
	Log.i("INFO", this.toString() + " STARTED");
	
	ComBus.subscribe(this);
	
	final EditText alias = (EditText) this.findViewById(R.id.aliasField);
	final ImageView av = (ImageView) this.findViewById(R.id.availableView);
	

	Button loginButton = (Button) this.findViewById(R.id.loginButton);

	loginButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
	    	//TODO remove later
	    	//login();
	    	
	    	ServerHandlerCtrl.getInstance().logInWithAlias("234566745fsdafsd");    	
	    }
	});

    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if(stateChange.equals(StateChanges.LOGGED_IN.toString())){
			login();
		}
		
	}

	private void login() {
		Intent intent = new Intent(this, MainViewActivity.class);
		startActivity(intent);
		
	}
}