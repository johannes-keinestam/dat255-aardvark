package edu.chalmers.aardvark.gui;

import java.util.List;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainViewActivity extends Activity implements
		edu.chalmers.aardvark.util.EventListener {
	private Contact startChatContact;

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

		drawContacts();
	}

	private void drawContacts() {
		List<Contact> contacts = ContactCtrl.getInstance().getContacts();
		
		for (Contact contact : contacts) {
			if((UserCtrl.getInstance().isOnline(contact))&&(ChatCtrl.getInstance().getChat(contact.getAardvarkID())!=null)){
				drawActive(contact);
			}
			else if((UserCtrl.getInstance().isOnline(contact))&&(ChatCtrl.getInstance().getChat(contact.getAardvarkID())==null)){
				drawOnline(contact);
			}
			else{
				drawOffline(contact);
			}
			
		}
	}

	private void drawActive(final Contact contact) {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inflater.inflate(R.layout.contactpanel, null);
		
		TextView tx = (TextView) item.findViewById(R.id.contactName);
		tx.setText(""+contact.getNickname()+" ("+contact.getAlias()+")");
		
		tx.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				showDialog(2);
				return true;
			}
		});
		tx.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startChatContact = contact;
				startChat();
			}
		});
		
		
		ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
		
	}

	private void drawOffline(Contact contact) {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.offline);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inflater.inflate(R.layout.contactpanel, null);
		
		TextView tx = (TextView) item.findViewById(R.id.contactName);
		tx.setText(""+contact.getNickname()+" ("+contact.getAlias()+")");
		ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	private void drawOnline(final Contact contact) {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inflater.inflate(R.layout.contactpanel, null);
		
		TextView tx = (TextView) item.findViewById(R.id.contactName);
		tx.setText(""+contact.getNickname()+" ("+contact.getAlias()+")");
		
		tx.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				showDialog(2);
				return true;
			}
		});
		tx.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ChatCtrl.getInstance().newChat(contact);
				startChatContact = contact;
			}
		});
		
		
		ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
		
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
			ServerHandlerCtrl.getInstance().logOut();
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
		case 2:
			final CharSequence[] items = { "Open chat", "Block", "Remove" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Contact");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Toast.makeText(getApplicationContext(),
							"Clicked " + items[item], Toast.LENGTH_SHORT)
							.show();
				}
			});
			dialog = builder.create();
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.LOGGED_OUT.toString())) {
			this.finish();
		}
		else if (stateChange.equals(StateChanges.CHAT_OPENED.toString())) {
			startChat();
		}

	}

	private void startChat() {
		Intent intent = new Intent(this, ChatViewActivity.class);
		intent.putExtra("aardwarkId", startChatContact.getAardvarkID());
		startActivity(intent);
		
	}
}
