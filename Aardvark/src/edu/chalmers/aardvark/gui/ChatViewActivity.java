package edu.chalmers.aardvark.gui;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatViewActivity extends Activity implements edu.chalmers.aardvark.util.EventListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.chatview);
	
	Log.i("INFO", this.toString() + " STARTED");
	
	ComBus.subscribe(this);

	LayoutInflater inflater = (LayoutInflater) this
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	LinearLayout ll = (LinearLayout) this.findViewById(R.id.messageLayout);

	for (int i = 1; i < 5; i++) {
	    View item;
	    if (i % 2 == 0) {
		item = inflater.inflate(R.layout.bubbleleftpanel, null);
	    } else {
		item = inflater.inflate(R.layout.bubblerightpanel, null);
	    }
	    final TextView tx = (TextView) item.findViewById(R.id.message);
	    tx.setText("Kalle" + i);
	    ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	LayoutInflater inflater = (LayoutInflater) this
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	LinearLayout ll = (LinearLayout) this.findViewById(R.id.messageLayout);
	ll.removeAllViews();

	for (int i = 1; i < 5; i++) {
	    View item;
	    if (i % 2 == 0) {
		item = inflater.inflate(R.layout.bubbleleftpanel, null);
	    } else {
		item = inflater.inflate(R.layout.bubblerightpanel, null);
	    }
	    final TextView tx = (TextView) item.findViewById(R.id.message);
	    tx.setText("resume" + i);
	    ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.chatmenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.addToContacts:
	case R.id.block:

	}
	return true;
    }

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if(stateChange.equals(StateChanges.NEW_MESSAGE_IN_CHAT.toString())){
			update();
		}
		else if(stateChange.equals(StateChanges.CONTACT_ADDED.toString())){
			contactAdded();
		}
		
	}

	private void contactAdded() {
		// TODO Auto-generated method stub
		
	}

	private void update() {
		// TODO Auto-generated method stub
		
	}
}
