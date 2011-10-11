package edu.chalmers.aardvark.gui;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ComBus;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainViewActivity extends Activity implements
		edu.chalmers.aardvark.util.EventListener {
	private User startChatContact;
	private ArrayList<String> online;
	private ArrayList<String> offline;
	private String lastLongPressedAardvarkID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);

		ComBus.subscribe(this);
		online = new ArrayList<String>();
		offline = new ArrayList<String>();
		
		for (Contact contact : ContactCtrl.getInstance().getContacts()) {
			offline.add(contact.getAardvarkID());
		}

		Log.i("CLASS", this.toString() + " STARTED");
		TextView aliasText = (TextView) this.findViewById(R.id.myUserNameLable);
		aliasText.setText(LocalUser.getLocalUser().getAlias());

		Button sb = (Button) this.findViewById(R.id.startChatButton);
		sb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(1);

			}
		});

		drawContacts();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		drawContacts();
	}

	private void drawContacts() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		ll.removeAllViews();
		ll = (LinearLayout) this.findViewById(R.id.offline);
		ll.removeAllViews();
		ll = (LinearLayout) this.findViewById(R.id.active);
		ll.removeAllViews();

		drawOnline();
		drawOffline();
		drawActive();

		Log.i("INFO", "draw");
	}

	@Override
	public void onBackPressed() {

		return;
	}

	private void drawActive() {
		List<Chat> chats = ChatCtrl.getInstance().getChats();
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.active);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (Chat chat : chats) {
			final User contact = chat.getRecipient();

			View item = inflater.inflate(R.layout.contactpanel, null);

			TextView tx = (TextView) item.findViewById(R.id.contactName);
			tx.setText(contact.getAlias());

			tx.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					startChatContact = contact;
					startChat();
				}
			});

			ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);

		}

	}

	private void drawOffline() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.offline);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			for (String aardvarkID : offline) {
					Log.i("INFO", "id::"+aardvarkID);
					Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);
					View item = inflater.inflate(R.layout.contactpanel, null);
					TextView tx = (TextView) item
							.findViewById(R.id.contactName);
					tx.setText(contact.getNickname());
					ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
				
			
		}
	}
	

	private void drawOnline() {
		Log.i("INFO", "drawonline");
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (final String aardvarkID : online) {
				final Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);
				Log.i("INFO", "drawonline loop");
				View item = inflater.inflate(R.layout.contactpanel, null);
				String alias = ServerHandlerCtrl.getInstance().getAlias(
						aardvarkID);
				TextView tx = (TextView) item.findViewById(R.id.contactName);
				tx.setText(alias+" ("+contact.getNickname()+")");

				tx.setOnLongClickListener(new OnLongClickListener() {

					public boolean onLongClick(View v) {
						lastLongPressedAardvarkID = aardvarkID;
						showDialog(2);
						return true;
					}
				});
				tx.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (ChatCtrl.getInstance().getChat(aardvarkID) == null) {
							ChatCtrl.getInstance().newChat(contact);
							startChatContact = contact;
						} else {
							startChatContact = contact;
							startChat();
						}

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
			dialog.setTitle("");
			Button startButton = (Button) dialog
					.findViewById(R.id.findUserButton);
			final EditText aliasField = (EditText) dialog
					.findViewById(R.id.findUserField);
			startButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String alias = aliasField.getText().toString();
					if (alias.length() == 0) {
						dismissDialog(1);
					} else {
						String aardvarkID = ServerHandlerCtrl.getInstance()
								.getAardvarkID(alias);
						if (aardvarkID != null) {
							User user = new User(alias, aardvarkID);
							startChatContact = user;
							if (ChatCtrl.getInstance().getChat(aardvarkID) == null) {
								ChatCtrl.getInstance().newChat(user);
							} else {
							}
							dismissDialog(1);
						} else {
							Log.i("INFO", "No user found");
						}

					}

				}

			});

			break;
		case 2:
			final CharSequence[] items = { "Block", "Remove" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Contact");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
						UserCtrl.getInstance().blockUser(lastLongPressedAardvarkID);
						break;
					case 1:
						ContactCtrl.getInstance().removeContact(lastLongPressedAardvarkID);
						dismissDialog(2);
						removeFromOnline(lastLongPressedAardvarkID);
						drawContacts();
						//TODO ext string
						Toast.makeText(getApplicationContext(),
								"Removed contact", Toast.LENGTH_SHORT)
								.show();
						break;

					default:
						break;
					}
				}
			});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.LOGGED_OUT.toString())) {
			this.finish();
		} else if (stateChange.equals(StateChanges.CHAT_OPENED.toString())) {
			Chat chat = (Chat) object;
			Log.i("INFO", " --opened");
			Log.i("INFO", chat.getRecipient().getAardvarkID() + " opened");
			startChatContact = (User) chat.getRecipient();

			startChat();
		} else if (stateChange.equals(StateChanges.CONTACT_ADDED.toString())) {
			drawContacts();

		} else if (stateChange.equals(StateChanges.USER_ONLINE.toString())) {
			String aardvarkID = (String) object;
			Log.i("INFO", aardvarkID);
			Log.i("INFO", "iscontact");
			if((isContact(aardvarkID))&&(!isInList(aardvarkID, online))){
				online.add(aardvarkID);
				removeFromOffline(aardvarkID);
			}
			drawContacts();
		} else if (stateChange.equals(StateChanges.USER_OFFLINE.toString())) {
			String aardvarkID = (String) object;
			Log.i("INFO", aardvarkID);
			if((isContact(aardvarkID))&&(!isInList(aardvarkID, offline))){
				offline.add(aardvarkID);
				removeFromOnline(aardvarkID);
			}
			drawContacts();
		} else if (stateChange.equals(StateChanges.USER_BLOCKED.toString())) {
			User blockedUser = (User) object;
			// TODO paint user as blocked
		} else if (stateChange.equals(StateChanges.USER_UNBLOCKED.toString())) {
			User blockedUser = (User) object;
			// TODO paint user as unblocked
		}
	}

	private void removeFromOnline(String aardvarkID) {

		for (int i = 0; i < online.size(); i++) {
			String aardvarkIDOn = online.get(i);
			if (aardvarkIDOn.equals(aardvarkID)) {
				online.remove(i);
			}
		}
	}
	private void removeFromOffline(String aardvarkID) {

		for (int i = 0; i < offline.size(); i++) {
			String aardvarkIDOn = offline.get(i);
			if (aardvarkIDOn.equals(aardvarkID)) {
				offline.remove(i);
			}
		}
	}
	private boolean isInList(String string, ArrayList<String> list){
		for (String aardvarkID : list) {
			if(aardvarkID.equals(string)){
				return true;
			}
		}
		return false;
	}

	private boolean isContact(String aardvarkID) {
		List<Contact> contacts = ContactCtrl.getInstance().getContacts();
		Log.i("INFO", "iscontact start");
		for (Contact contact : contacts) {
			if (contact.getAardvarkID().equals(aardvarkID)) {
				Log.i("INFO", "iscontact true");
				return true;
			}

		}
		return false;

	}

	private void startChat() {
		Intent intent = new Intent(this, ChatViewActivity.class);
		Log.i("INFO", startChatContact.getAardvarkID() + " start chat");
		intent.putExtra("aardvarkID", startChatContact.getAardvarkID());
		
		startActivity(intent);

	}
}
