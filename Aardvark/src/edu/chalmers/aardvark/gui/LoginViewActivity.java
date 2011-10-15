/**
 * Copyright 2011 Fredrik Hidstrand, Johannes Keinestam, Magnus Sjöqvist, Fredrik Thander
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * Activity class controlling and displaying the login view. Lets the user log
 * in to the server by inputing an alias.
 * 
 * Listens for changes in model.
 */
public class LoginViewActivity extends Activity implements EventListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i("CLASS", this.toString() + " STARTED");

		// Subscribes as listener for data change in model
		ComBus.subscribe(this);

		Button loginButton = (Button) this.findViewById(R.id.loginButton);

		// Logs in with the inputted alias when Login button clicked
		loginButton.setOnClickListener(new OnClickListener() {
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
			// User logged in, hides progress dialog and opens MainView.
			dismissDialog(1);
			login();
		} else if (stateChange.equals(StateChanges.LOGIN_FAILED.toString())) {
			// Login failed, hides progress dialog and shows notification.
			dismissDialog(1);
			loginFailed();
		} else if (stateChange.equals(StateChanges.ALIAS_UNAVAILABLE.toString())) {
			// Alias was unavailable, hides progress dialog and shows
			// notification.
			dismissDialog(1);
			aliasUnavailable();
		}
	}

	/**
	 * Opens the MainView. Used when login was successful.
	 */
	private void login() {
		Intent intent = new Intent(this, MainViewActivity.class);
		startActivity(intent);
	}

	/**
	 * Shows Log in failed notification. Used when login was unsuccessful.
	 */
	private void loginFailed() {
		Toast.makeText(AardvarkApp.getContext(), getString(R.string.loginFailedLoginView),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Shows Alias is unavailable notification. Used when alias reported as
	 * already taken.
	 */
	private void aliasUnavailable() {
		Toast.makeText(AardvarkApp.getContext(), getString(R.string.aliasUnavailableLoginView),
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case 1:
			// Creates dialog with an animated spinner
			ProgressDialog progDialog = new ProgressDialog(this);
			progDialog.setCancelable(false);
			progDialog.setMessage(getString(R.string.loggingInLoginView));
			dialog = progDialog;
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
}