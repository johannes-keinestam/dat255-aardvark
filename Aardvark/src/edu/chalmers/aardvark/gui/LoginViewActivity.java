package edu.chalmers.aardvark.gui;

import java.util.Timer;

import edu.chalmers.aardvark.AardvarkApp;
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
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginViewActivity extends Activity implements EventListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	
	Log.i("CLASS", this.toString() + " STARTED");
	
	ComBus.subscribe(this);
	
	final EditText aliasInput = (EditText) this.findViewById(R.id.aliasField);
	final ImageView av = (ImageView) this.findViewById(R.id.availableView);
	
	
	aliasInput.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.i("INFO", "alias on"+s.toString());
//			if(ServerHandlerCtrl.getInstance().isAliasAvailable("6")){
//				Log.i("INFO", "alias is avabadadasda");
//			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			Log.i("INFO", "alias after"+s.toString());
			
		}
	});
		

	Button loginButton = (Button) this.findViewById(R.id.loginButton);

	loginButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		EditText aliasInput = (EditText) findViewById(R.id.aliasField);
		String alias = aliasInput.getText().toString();
		
	    	ServerHandlerCtrl.getInstance().logInWithAlias(alias);    	
	    }
	});

    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.LOGGED_IN.toString())) {
			login();
		} else if (stateChange.equals(StateChanges.LOGIN_FAILED.toString())) {
		    loginFailed();
		} else if (stateChange.equals(StateChanges.ALIAS_UNAVAILABLE.toString())) {
		    aliasUnavailable();
		}
		
	}

	private void login() {
		Intent intent = new Intent(this, MainViewActivity.class);
		startActivity(intent);
	}
	
	private void loginFailed() {
	    Toast.makeText(AardvarkApp.getContext(), "Log in failed! Check your network connection and try again.", Toast.LENGTH_LONG).show();
	}
	
	private void aliasUnavailable() {
	    Toast.makeText(AardvarkApp.getContext(), "Alias unavailable! Try another.", Toast.LENGTH_LONG).show();
	}
}