package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainViewActivity extends Activity implements edu.chalmers.aardvark.util.EventListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mainview);
	
	Log.i("INFO", this.toString() + " STARTED");

	Button sb = (Button) this.findViewById(R.id.startChatButton);
	sb.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		showDialog(1);

	    }
	});

	drawOnlineContacts();
	drawOfflineContacts();

    }

    private void drawOnlineContacts() {
	LayoutInflater inflater = (LayoutInflater) this
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
	final Intent intent = new Intent(this, ChatViewActivity.class);

	for (int i = 1; i < 5; i++) {
	    View item = inflater.inflate(R.layout.contactpanel, null);
	    final TextView tx = (TextView) item.findViewById(R.id.contactName);
	    tx.setText("Kalle" + i);
	    final int j = i;
	    tx.setOnClickListener(new OnClickListener() {

		public void onClick(View v) {
		    tx.setText("pressed" + j);
		    startActivity(intent);

		}
	    });
	    ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
	}
    }

    private void drawOfflineContacts() {

	LinearLayout ll = (LinearLayout) this.findViewById(R.id.offline);
	LayoutInflater inflater = (LayoutInflater) this
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	for (int i = 1; i < 5; i++) {
	    View item = inflater.inflate(R.layout.contactpanel, null);
	    final TextView tx = (TextView) item.findViewById(R.id.contactName);
	    tx.setText("Sven" + i);
	    final int j = i;
	    tx.setOnClickListener(new OnClickListener() {

		public void onClick(View v) {
		    tx.setText("pressed" + j);

		}
	    });
	    ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.mainmenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.newChat:
	    showDialog(1);
	    break;
	case R.id.settings:
	    Intent intent = new Intent(this, SettingsViewActivity.class);
	    startActivity(intent);
	    break;
	case R.id.logout:
	    // TODO
	    break;

	}
	return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	Dialog dialog;
	switch (id) {
	case 1:
	    dialog = new Dialog(this);

	    dialog.setContentView(R.layout.newchatdialog);
	    dialog.setTitle("Custom Dialog");

	    break;
	default:
	    dialog = null;
	}
	return dialog;
    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		// TODO Auto-generated method stub
		
	}
}
