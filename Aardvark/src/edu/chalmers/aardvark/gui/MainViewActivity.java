package edu.chalmers.aardvark.gui;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

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
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainViewActivity extends Activity implements
		edu.chalmers.aardvark.util.EventListener {
	private User startChatContact;
	private ArrayList<String> online;
	private ArrayList<String> offline;
	private String lastLongPressedAardvarkID;
	private boolean done = false;
	private boolean requestedChat = false;

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
		
		getOnlineUsers();
		drawContacts();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		drawContacts();
	}
	private void getOnlineUsers(){
		if(!done){
		for (RosterEntry user : ServerHandlerCtrl.getInstance().getOnlineUsers()) {
			Roster roster = ServerConnection.getConnection().getRoster();
			Presence presence = roster.getPresence(user.getUser());
			if (presence.isAvailable()) {
				String temp = user.getUser();
				String aardvarkID = temp.substring(0, temp.lastIndexOf("@"));
				ComBus.notifyListeners(StateChanges.USER_ONLINE.toString(), aardvarkID);
			}
		}
		done = true;
		}
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

	}

	@Override
	public void onBackPressed() {
		// Do nothing. Don't allow going back to login view without logging out.
		return;
	}

	private void drawActive() {
		List<Chat> chats = ChatCtrl.getInstance().getChats();
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.active);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (Chat chat : chats) {
			final User user = chat.getRecipient();
			Contact contact = ContactCtrl.getInstance().getContact(user.getAardvarkID());
			String alias = ServerHandlerCtrl.getInstance().getAlias(
					user.getAardvarkID());

			View item = inflater.inflate(R.layout.chatpanel, null);

			TextView tx = (TextView) item.findViewById(R.id.chatName);
			
			if(contact!=null){
				tx.setText(alias+"("+contact.getNickname()+")");
			}
			else{
				tx.setText(alias);
			}
			if (chat.unreadMessages() > 0) {
				TextView tv = (TextView) item.findViewById(R.id.messageView);
				tv.setText("("+chat.unreadMessages()+")");
				tv.setVisibility(TextView.VISIBLE);
			}
			
			tx.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					startChatContact = user;
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

			for (final String aardvarkID : offline) {
					Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);
					View item = inflater.inflate(R.layout.contactpanel, null);
					TextView tx = (TextView) item
							.findViewById(R.id.contactName);
					tx.setText(contact.getNickname());
					if(UserCtrl.getInstance().isUserBlocked(aardvarkID)){
						ImageView iv = (ImageView) item.findViewById(R.id.blockView);
						iv.setVisibility(ImageView.VISIBLE);
					}
					tx.setOnLongClickListener(new OnLongClickListener() {

						public boolean onLongClick(View v) {
							lastLongPressedAardvarkID = aardvarkID;
							showContactDialog();
							return true;
						}
					});
					ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
				
			
		}
	}
	private void showContactDialog() {
		if(UserCtrl.getInstance().isUserBlocked(lastLongPressedAardvarkID)){
			showDialog(3);
		}
		else{
			showDialog(2);
		}
		
	}

	private void drawOnline() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (final String aardvarkID : online) {
				final Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);
				View item = inflater.inflate(R.layout.contactpanel, null);
				TextView tx = (TextView) item.findViewById(R.id.contactName);
				tx.setText(contact.getNickname());
				if(UserCtrl.getInstance().isUserBlocked(aardvarkID)){
					ImageView iv = (ImageView) item.findViewById(R.id.blockView);
					iv.setVisibility(ImageView.VISIBLE);
				}
				
				tx.setOnLongClickListener(new OnLongClickListener() {

					public boolean onLongClick(View v) {
						lastLongPressedAardvarkID = aardvarkID;
						showContactDialog();
						return true;
					}

					
				});
				tx.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (ChatCtrl.getInstance().getChat(aardvarkID) == null) {
							requestedChat = true;
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
			showDialog(4);
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
							requestedChat = true;
							startChatContact = user;
							if (ChatCtrl.getInstance().getChat(aardvarkID) == null) {
								ChatCtrl.getInstance().newChat(user);
							} else {
							}
							dismissDialog(1);
						} else {
						    Toast.makeText(getApplicationContext(),
							   getString(R.string.userNotFoundMainView), Toast.LENGTH_SHORT).show();
						}

					}

				}

			});

			break;
		case 2:
		
			final CharSequence[] items = { getString(R.string.blockMainView), getString(R.string.renameMainView), getString(R.string.removeMainView) };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.contactMainView));
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
							UserCtrl.getInstance().blockUser(lastLongPressedAardvarkID);
						drawContacts();
						break;
					case 1:
			            showRenameDialog();
						break;
					case 2:
						ContactCtrl.getInstance().removeContact(lastLongPressedAardvarkID);
						dialog.dismiss();
						removeFromOnline(lastLongPressedAardvarkID);
						removeFromOffline(lastLongPressedAardvarkID);
						drawContacts();
						//TODO ext string
						Toast.makeText(getApplicationContext(),
								getString(R.string.removedContactNotificationMainView), Toast.LENGTH_SHORT)
								.show();
						break;

					default:
						break;
					}
				}
			});
			dialog = builder.create();
			break;
		case 3:
			final CharSequence[] itemsUb = { getString(R.string.unblockMainView), getString(R.string.renameMainView), getString(R.string.removeMainView) };
			AlertDialog.Builder builderUb = new AlertDialog.Builder(this);
			builderUb.setTitle(getString(R.string.contactMainView));
			builderUb.setItems(itemsUb, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
						UserCtrl.getInstance().unblockUser(lastLongPressedAardvarkID);
						drawContacts();
						break;
					case 1:
						
						break;
					case 2:
						ContactCtrl.getInstance().removeContact(lastLongPressedAardvarkID);
						dialog.dismiss();
						removeFromOnline(lastLongPressedAardvarkID);
						removeFromOffline(lastLongPressedAardvarkID);
						drawContacts();
						Toast.makeText(getApplicationContext(),
								getString(R.string.removedContactNotificationMainView), Toast.LENGTH_SHORT)
								.show();
						break;

					default:
						break;
					}
				}
			});
			dialog = builderUb.create();
			break;
		case 4:
			 ProgressDialog progDialog = new ProgressDialog(this);
			 progDialog.setCancelable(false);
			progDialog.setMessage(getString(R.string.loggingOutMainView));
			dialog = progDialog;
			break;
		case 5:
			AlertDialog.Builder alert = new AlertDialog.Builder(this)
	        	.setMessage(String.format(getString(R.string.acceptChatTextMainView), ServerHandlerCtrl.getInstance().getAlias(startChatContact.getAardvarkID())))
	        	.setPositiveButton(getString(R.string.acceptChatYesMainView), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	startChat();
	                }
	        	})
	        	.setNegativeButton(getString(R.string.acceptChatBlockMainView), new DialogInterface.OnClickListener() {
	        		public void onClick(DialogInterface dialog, int whichButton) {
	        			Chat unwantedChat = ChatCtrl.getInstance().getChat(startChatContact.getAardvarkID());
	        			ChatCtrl.getInstance().closeChat(unwantedChat);
	        			UserCtrl.getInstance().blockUser(startChatContact);
	        		}
	        	});

	        dialog = alert.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.LOGGED_OUT.toString())) {
			dismissDialog(4);
			this.finish();
		} else if (stateChange.equals(StateChanges.CHAT_OPENED.toString())) {
			Chat chat = (Chat) object;
			Log.i("INFO", chat.getRecipient().getAardvarkID() + " chat opened");
			startChatContact = (User) chat.getRecipient();
			if (!isContact(startChatContact.getAardvarkID()) && !requestedChat) {
				showDialog(5);
			} else {
				requestedChat = false;
				startChat();
			}
		} else if (stateChange.equals(StateChanges.CHAT_CLOSED.toString())) {
			Chat chat = (Chat) object;
			Log.i("INFO", chat.getRecipient().getAardvarkID() + " chat closed");
			drawContacts();
		} else if (stateChange.equals(StateChanges.CONTACT_ADDED.toString())) {
		    Contact contact = (Contact) object;
		    online.add(contact.getAardvarkID());
		    
		    drawContacts();
		} else if (stateChange.equals(StateChanges.CONTACT_RENAMED.toString())) {
			drawContacts();
		} else if (stateChange.equals(StateChanges.USER_ONLINE.toString())) {
			String aardvarkID = (String) object;
			if((isContact(aardvarkID))&&(!isInList(aardvarkID, online))){
				online.add(aardvarkID);
				removeFromOffline(aardvarkID);
			}
			drawContacts();
		} else if (stateChange.equals(StateChanges.USER_OFFLINE.toString())) {
			String aardvarkID = (String) object;
			if((isContact(aardvarkID))&&(!isInList(aardvarkID, offline))){
				offline.add(aardvarkID);
				removeFromOnline(aardvarkID);
			}
			drawContacts();
		} else if (stateChange.equals(StateChanges.USER_BLOCKED.toString())) {
			drawContacts();
		} else if (stateChange.equals(StateChanges.USER_UNBLOCKED.toString())) {
			drawContacts();
		} else if (stateChange.equals(StateChanges.NEW_MESSAGE_IN_CHAT.toString())) {
			drawContacts();
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
		for (Contact contact : contacts) {
			if (contact.getAardvarkID().equals(aardvarkID)) {
				return true;
			}

		}
		return false;

	}

	private void startChat() {
		ChatCtrl.getInstance().getChat(startChatContact.getAardvarkID()).clearUnreadMessages();
		Intent intent = new Intent(this, ChatViewActivity.class);
		intent.putExtra(getString(R.string.aardvarkIntentExtraName), startChatContact.getAardvarkID());
		
		startActivity(intent);
	}
	
	private void showRenameDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.renameContactTitleMainView));
        alert.setMessage(getString(R.string.renameContactTextMainView));

        // Set an EditText view to get user input 
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(getString(R.string.renameContactOKMainView), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  String value = input.getText().toString();
                  ContactCtrl.getInstance().setNickname(lastLongPressedAardvarkID, value);
                }
        });

        alert.setNegativeButton(getString(R.string.renameContactCancelMainView), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Cancelled
          }
        });

        alert.show();
	}
}
