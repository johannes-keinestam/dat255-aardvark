package edu.chalmers.aardvark.gui;


import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.EventListener;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginViewActivity extends Activity implements EventListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	
	Log.i("CLASS", this.toString() + " STARTED");
	
	ComBus.subscribe(this);	

	Button loginButton = (Button) this.findViewById(R.id.loginButton);

	loginButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		EditText aliasInput = (EditText) findViewById(R.id.aliasField);
		String alias = aliasInput.getText().toString();
		
	    	ServerHandlerCtrl.getInstance().logInWithAlias(alias);
	    	showDialog(1);
	    }
	});

    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.LOGGED_IN.toString())) {
		    dismissDialog(1);
		    login();
		} else if (stateChange.equals(StateChanges.LOGIN_FAILED.toString())) {
		    dismissDialog(1);
		    loginFailed();
		} else if (stateChange.equals(StateChanges.ALIAS_UNAVAILABLE.toString())) {
		    dismissDialog(1);
		    aliasUnavailable();
		}
		
	}

	private void login() {
		Intent intent = new Intent(this, MainViewActivity.class);
		startActivity(intent);
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
        		case 1:
        		    ProgressDialog progDialog = new ProgressDialog(this);
        		    progDialog.setCancelable(false);
        		    progDialog.setMessage("Logging in...");
        		    dialog = progDialog;
        		    break;
        		default:
        		    dialog = null;
		}
		return dialog;
	}
	
	private void loginFailed() {
	    Toast.makeText(AardvarkApp.getContext(), "Log in failed! Check your network connection and try again.", Toast.LENGTH_LONG).show();
	}
	
	private void aliasUnavailable() {
	    Toast.makeText(AardvarkApp.getContext(), "Alias unavailable! Try another.", Toast.LENGTH_LONG).show();
	}
}