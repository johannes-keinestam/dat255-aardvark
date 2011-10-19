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
import edu.chalmers.aardvark.util.EventListener;
import edu.chalmers.aardvark.util.ServerConnection;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

/**
 * Activity class controlling and displaying the main view. It is the main hub
 * of the application, letting the user manage contacts, open chats and the
 * settings menu.
 * 
 * Listens for changes in model.
 */
public class MainViewActivity extends Activity implements EventListener {
	/** Represents the user which a chat was requested with */
	private User startChatContact;
	/** List of online contacts for drawing contact list */
	private ArrayList<String> online;
	/** List of offline contacts for drawing contact list */
	private ArrayList<String> offline;
	/** Represents the contact that was last pressed */
	private String lastLongPressedAardvarkID;
	/** Boolean representing the state of loading contact statuses */
	private boolean isStatusLoadingDone = false;
	/** Boolean true if user has requested to open a new chat */
	private boolean requestedChat = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);

		Log.i("CLASS", this.toString() + " STARTED");

		// Subscribes as listener for data change in model
		ComBus.subscribe(this);

		// Loads contacts by loading them to offline list
		online = new ArrayList<String>();
		offline = new ArrayList<String>();
		for (Contact contact : ContactCtrl.getInstance().getContacts()) {
			offline.add(contact.getAardvarkID());
		}

		// Displays logged in alias at top of screen
		TextView aliasText = (TextView) this.findViewById(R.id.myUserNameLable);
		aliasText.setText(LocalUser.getLocalUser().getAlias());

		// Makes the open new chat button open dialog.
		Button sb = (Button) this.findViewById(R.id.startChatButton);
		sb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(1);
			}
		});

		getOnlineUsers();
		drawLists();
	}

	@Override
	protected void onResume() {
		super.onResume();
		drawLists();
	}

	/**
	 * Gets the online status of all contacts.
	 */
	private void getOnlineUsers() {
		// Only loads status if status loading isn't already completed.
		if (!isStatusLoadingDone) {
			for (RosterEntry user : ServerHandlerCtrl.getInstance().getRegisteredUsers()) {
				Roster roster = ServerConnection.getConnection().getRoster();
				Presence presence = roster.getPresence(user.getUser());
				if (presence.isAvailable()) {
					String temp = user.getUser();
					String aardvarkID = temp.substring(0, temp.lastIndexOf("@"));
					ComBus.notifyListeners(StateChanges.USER_ONLINE.toString(), aardvarkID);
				}
			}
			isStatusLoadingDone = true;
		}
	}

	/**
	 * Draws Active chat, offline and online contacts lists.
	 */
	private void drawLists() {
		// Clears all current items
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		ll.removeAllViews();
		ll = (LinearLayout) this.findViewById(R.id.offline);
		ll.removeAllViews();
		ll = (LinearLayout) this.findViewById(R.id.active);
		ll.removeAllViews();

		// Draws.
		drawOnline();
		drawOffline();
		drawActive();

	}

	@Override
	public void onBackPressed() {
		// Do nothing. Don't allow going back to login view without logging out.
		return;
	}

	/**
	 * Draws Active chats list by loading currently added chats.
	 */
	private void drawActive() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.active);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Add item for each open chat
		List<Chat> chats = ChatCtrl.getInstance().getChats();
		for (Chat chat : chats) {
			final User user = chat.getRecipient();
			Contact contact = ContactCtrl.getInstance().getContact(user.getAardvarkID());
			String alias = ServerHandlerCtrl.getInstance().getAlias(user.getAardvarkID());

			View item = inflater.inflate(R.layout.chatpanel, null);
			TextView tx = (TextView) item.findViewById(R.id.chatName);

			String printedName = alias;
			if (alias == null || alias.trim().length() == 0) {
				printedName = getString(R.string.unknownUserMainView);
			}
			// Add nickname if user is contact.
			if (contact != null) {
				tx.setText(printedName + "(" + contact.getNickname() + ")");
			} else {
				tx.setText(printedName);
			}

			// If chat has unread messages, display number to the right.
			if (chat.unreadMessages() > 0) {
				TextView tv = (TextView) item.findViewById(R.id.messageView);
				tv.setText("(" + chat.unreadMessages() + ")");
				tv.setVisibility(TextView.VISIBLE);
			}

			// Open chat if an active chat is clicked.
			tx.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startChatContact = user;
					startChat();
				}
			});

			ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		// Adds guide text if no chats opened
		if (chats.size() == 0) {
			TextView guideMessage = new TextView(this);
			guideMessage.setText(R.string.openChatGuideMessage);
			guideMessage.setTextColor(Color.GRAY);
			guideMessage.setPadding(2, 8, 2, 8);
			ll.addView(guideMessage, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	/**
	 * Draws Offline contacts list.
	 */
	private void drawOffline() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.offline);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Add item for each offline contact.
		for (final String aardvarkID : offline) {
			Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);

			// Display nickname on list item.
			View item = inflater.inflate(R.layout.contactpanel, null);
			TextView tx = (TextView) item.findViewById(R.id.contactName);
			// Prevents system crash in a very unusual situation.
			try {
				tx.setText(contact.getNickname());
			} catch (NullPointerException e) {
			}

			// If contact is blocked, add block graphic on the right.
			if (UserCtrl.getInstance().isUserBlocked(aardvarkID)) {
				ImageView iv = (ImageView) item.findViewById(R.id.blockView);
				iv.setVisibility(ImageView.VISIBLE);
			}

			// Show Contacts options menu if contact long-clicked.
			tx.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					lastLongPressedAardvarkID = aardvarkID;
					showContactDialog();
					return true;
				}
			});

			ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		// Adds guide text if no chats opened
		if (offline.size() == 0) {
			TextView guideMessage = new TextView(this);
			guideMessage.setText(R.string.emptyContactList);
			guideMessage.setTextColor(Color.GRAY);
			guideMessage.setPadding(2, 8, 2, 8);
			ll.addView(guideMessage, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	/**
	 * Shows Contact options dialog. Shows Unblock option if contact is blocked,
	 * Block option if not.
	 */
	private void showContactDialog() {
		if (UserCtrl.getInstance().isUserBlocked(lastLongPressedAardvarkID)) {
			showDialog(3);
		} else {
			showDialog(2);
		}
	}

	/**
	 * Draws Online contacts list.
	 */
	private void drawOnline() {
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.online);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Adds item for each online contact.
		for (final String aardvarkID : online) {
			final Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);

			// Displays nickname on list item.
			View item = inflater.inflate(R.layout.contactpanel, null);
			TextView tx = (TextView) item.findViewById(R.id.contactName);
			// Prevents system crash in a very unusual situation.
			try {
				tx.setText(contact.getNickname());
			} catch (NullPointerException e) {
			}

			// If user is blocked, show block graphic on the right.
			if (UserCtrl.getInstance().isUserBlocked(aardvarkID)) {
				ImageView iv = (ImageView) item.findViewById(R.id.blockView);
				iv.setVisibility(ImageView.VISIBLE);
			}

			// Show Contact dialog when contact is long-clicked.
			tx.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View v) {
					lastLongPressedAardvarkID = aardvarkID;
					showContactDialog();
					return true;
				}

			});

			// Start chat if user is clicked.
			tx.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Start new chat if none exists, otherwise open existing.
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

		if (online.size() == 0) {
			TextView guideMessage = new TextView(this);
			guideMessage.setText(R.string.emptyContactList);
			guideMessage.setTextColor(Color.GRAY);
			guideMessage.setPadding(2, 8, 2, 8);
			ll.addView(guideMessage, ViewGroup.LayoutParams.WRAP_CONTENT);
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
			// New chat selected in menu. Shows New chat dialog.
			showDialog(1);
			break;
		case R.id.settings:
			// Settings selected in menu. Opens Settings View.
			Intent intent = new Intent(this, SettingsViewActivity.class);
			startActivity(intent);
			break;
		case R.id.logout:
			// Log out selected in menu. Shows progress dialog and logs out.
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
			// Creates New Chat dialog.
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.newchatdialog);
			dialog.setTitle("");

			Button startButton = (Button) dialog.findViewById(R.id.findUserButton);
			final EditText aliasField = (EditText) dialog.findViewById(R.id.findUserField);

			// Start chat with specified alias if user clicked OK.
			startButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String alias = aliasField.getText().toString();

					// Do nothing if alias not inputted.
					if (alias.length() == 0) {
						dismissDialog(1);
					} else {
						String aardvarkID = ServerHandlerCtrl.getInstance().getAardvarkID(alias);
						// Opens chat (new or existing) if alias exists, and
						// user is online.
						// Otherwise, notify user that alias doesn't exist.
						if (aardvarkID != null
								&& ServerHandlerCtrl.getInstance().isOnline(aardvarkID)) {
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
									getString(R.string.userNotFoundMainView), Toast.LENGTH_SHORT)
									.show();
						}
					}
				}
			});

			break;
		case 2:
			// Creates Contact dialog with Block option.
			final CharSequence[] items = { getString(R.string.blockMainView),
					getString(R.string.renameMainView), getString(R.string.removeMainView) };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.contactMainView));

			// Add listener to each menu option.
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
						// Block clicked. Blocks user.
						UserCtrl.getInstance().blockUser(lastLongPressedAardvarkID);
						drawLists();
						break;
					case 1:
						// Rename clicked. Shows rename contact dialog.
						showRenameDialog();
						break;
					case 2:
						// Remove clicked. Removes contact from list.
						ContactCtrl.getInstance().removeContact(lastLongPressedAardvarkID);
						dialog.dismiss();
						removeFromOnline(lastLongPressedAardvarkID);
						removeFromOffline(lastLongPressedAardvarkID);
						drawLists();

						// Notifies user
						Toast.makeText(getApplicationContext(),
								getString(R.string.removedContactNotificationMainView),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				}
			});
			dialog = builder.create();

			break;
		case 3:
			// Creates Contact dialog with Unblock option.
			final CharSequence[] itemsUb = { getString(R.string.unblockMainView),
					getString(R.string.renameMainView), getString(R.string.removeMainView) };

			AlertDialog.Builder builderUb = new AlertDialog.Builder(this);
			builderUb.setTitle(getString(R.string.contactMainView));

			// Add listener to each menu option.
			builderUb.setItems(itemsUb, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
						// Unblock clicked. Unblocks user.
						UserCtrl.getInstance().unblockUser(lastLongPressedAardvarkID);
						drawLists();
						break;
					case 1:
						// Rename clicked. Shows rename contact dialog.
						showRenameDialog();
						break;
					case 2:
						// Remove clicked. Removes contact from list.
						ContactCtrl.getInstance().removeContact(lastLongPressedAardvarkID);
						dialog.dismiss();
						removeFromOnline(lastLongPressedAardvarkID);
						removeFromOffline(lastLongPressedAardvarkID);
						drawLists();

						// Notifies user.
						Toast.makeText(getApplicationContext(),
								getString(R.string.removedContactNotificationMainView),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				}
			});
			dialog = builderUb.create();

			break;
		case 4:
			// Creates Logging out progress dialog.
			ProgressDialog progDialog = new ProgressDialog(this);
			progDialog.setCancelable(false);
			progDialog.setMessage(getString(R.string.loggingOutMainView));
			dialog = progDialog;

			break;
		case 5:
			// Creates Start chat confirm dialog.
			AlertDialog.Builder alert = new AlertDialog.Builder(this)
					.setMessage(
							String.format(
									getString(R.string.acceptChatTextMainView),
									ServerHandlerCtrl.getInstance().getAlias(
											startChatContact.getAardvarkID())))
					// Starts chat if Yes was clicked.
					.setPositiveButton(getString(R.string.acceptChatYesMainView),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									startChat();
								}
							})
					// Blocks user and closes chat if Block was clicked.
					.setNegativeButton(getString(R.string.acceptChatBlockMainView),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									Chat unwantedChat = ChatCtrl.getInstance().getChat(
											startChatContact.getAardvarkID());
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
			// Local user logged out. Exits current view and shows Login View.
			try {
				// try to close logout dialog
				dismissDialog(4);
			} catch (IllegalArgumentException e) {
			}
			// Show login view
			Intent intent = new Intent(this, LoginViewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			this.finish();
		} else if (stateChange.equals(StateChanges.CHAT_OPENED.toString())) {
			// New chat created.
			Chat chat = (Chat) object;
			Log.i("INFO", chat.getRecipient().getAardvarkID() + " chat opened");
			startChatContact = (User) chat.getRecipient();

			// Shows dialog asking the user if they want to start the chat
			// if user is not a contact or chat was not explicitly requested.
			// Otherwise just opens chat,
			if (!isContact(startChatContact.getAardvarkID()) && !requestedChat) {
				// Try to show dialog. If activity is finished, may produce
				// crash.
				try {
					showDialog(5);
				} catch (Exception e) {
				}
			} else {
				requestedChat = false;
				startChat();
			}
		} else if (stateChange.equals(StateChanges.CHAT_CLOSED.toString())) {
			// Chat removed from Active chats. Removes from list.
			Chat chat = (Chat) object;
			Log.i("INFO", chat.getRecipient().getAardvarkID() + " chat closed");
			drawLists();
		} else if (stateChange.equals(StateChanges.CONTACT_ADDED.toString())) {
			// New contact added. Adds to list.
			Contact contact = (Contact) object;
			online.add(contact.getAardvarkID());
			drawLists();
		} else if (stateChange.equals(StateChanges.CONTACT_RENAMED.toString())) {
			// Existing contact renamed. Redraws lists.
			drawLists();
		} else if (stateChange.equals(StateChanges.USER_ONLINE.toString())) {
			// A user logged in. Adds to Online contacts if user is contact.
			String aardvarkID = (String) object;
			if ((isContact(aardvarkID)) && (!isInList(aardvarkID, online))) {
				online.add(aardvarkID);
				removeFromOffline(aardvarkID);
			}
			drawLists();
		} else if (stateChange.equals(StateChanges.USER_OFFLINE.toString())) {
			// A user logged out. Adds to Offline contacts if user is contact.
			String aardvarkID = (String) object;
			if ((isContact(aardvarkID)) && (!isInList(aardvarkID, offline))) {
				offline.add(aardvarkID);
				removeFromOnline(aardvarkID);
			}
			drawLists();
		} else if (stateChange.equals(StateChanges.USER_BLOCKED.toString())) {
			// User was blocked. Redraws lists.
			drawLists();
		} else if (stateChange.equals(StateChanges.USER_UNBLOCKED.toString())) {
			// User was unblocked. Redraws lists.
			drawLists();
		} else if (stateChange.equals(StateChanges.NEW_MESSAGE_IN_CHAT.toString())) {
			// A new message was received. Redraws lists (and thus,
			// the unread messages count).
			drawLists();
		}
	}

	/**
	 * Removes a contact with the given AardvarkID from the Online contacts
	 * list.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to remove.
	 */
	private void removeFromOnline(String aardvarkID) {
		for (int i = 0; i < online.size(); i++) {
			String aardvarkIDOn = online.get(i);
			if (aardvarkIDOn.equals(aardvarkID)) {
				online.remove(i);
			}
		}
	}

	/**
	 * Removes a contact with the given AardvarkID from the Offline contacts
	 * list.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to remove.
	 */
	private void removeFromOffline(String aardvarkID) {
		for (int i = 0; i < offline.size(); i++) {
			String aardvarkIDOf = offline.get(i);
			if (aardvarkIDOf.equals(aardvarkID)) {
				offline.remove(i);
			}
		}
	}

	/**
	 * Helper method for testing if a given String is in a list.
	 * 
	 * @param string
	 *            string to search for.
	 * @param list
	 *            list to search in.
	 * @return true if string is found, false if not.
	 */
	private boolean isInList(String string, List<String> list) {
		for (String aardvarkID : list) {
			if (aardvarkID.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether an AardvarkID is currently shown as a contact in the GUI.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return true if AardvarkID is shown as contact, false if not.
	 */
	private boolean isContact(String aardvarkID) {
		List<Contact> contacts = ContactCtrl.getInstance().getContacts();
		for (Contact contact : contacts) {
			if (contact.getAardvarkID().equals(aardvarkID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Opens a chat window with requested user.
	 * 
	 * Note: user to be set in the startChatContact field.
	 */
	private void startChat() {
		try {
			dismissDialog(5);
		} catch (IllegalArgumentException iae) {
		}

		// Always set all messages to read when opening chat window.
		ChatCtrl.getInstance().getChat(startChatContact.getAardvarkID()).clearUnreadMessages();

		Intent intent = new Intent(this, ChatViewActivity.class);
		intent.putExtra(getString(R.string.aardvarkIntentExtraName),
				startChatContact.getAardvarkID());
		startActivity(intent);
	}

	/**
	 * Shows Rename contact dialog.
	 */
	private void showRenameDialog() {
		// Create dialog.
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(getString(R.string.renameContactTitleMainView));
		alert.setMessage(getString(R.string.renameContactTextMainView));

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		// Rename contact if OK button clicked.
		alert.setPositiveButton(getString(R.string.renameContactOKMainView),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						ContactCtrl.getInstance().setNickname(lastLongPressedAardvarkID, value);
					}
				});

		// Do nothing if Cancel button clicked.
		alert.setNegativeButton(getString(R.string.renameContactCancelMainView),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Cancelled
					}
				});

		// Show dialog.
		alert.show();
	}
}
