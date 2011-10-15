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

import java.util.List;

import edu.chalmers.aardvark.R;
import edu.chalmers.aardvark.ctrl.ChatCtrl;
import edu.chalmers.aardvark.ctrl.ContactCtrl;
import edu.chalmers.aardvark.ctrl.ServerHandlerCtrl;
import edu.chalmers.aardvark.ctrl.UserCtrl;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.Contact;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.EventListener;
import edu.chalmers.aardvark.util.StateChanges;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

/**
 * Activity class controlling and displaying the chat view. Displays a list of
 * messages as bubbles and allows the user to add recipient to contacts and/or
 * blocklist. Basically represents a Chat object graphically.
 * 
 * Listens for changes in model.
 */
public class ChatViewActivity extends Activity implements EventListener {
	/** String of the AardvarkID associated with the chat */
	private String aardvarkID;
	/** String of the alias associated with the chat */
	private String alias;
	/** Keeps track whether the current chat view is shown */
	private boolean visible = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatview);

		Log.i("CLASS", this.toString() + " STARTED");

		// Gets the user information associated with the chat
		aardvarkID = getIntent().getExtras().getString(getString(R.string.aardvarkIntentExtraName));
		alias = ServerHandlerCtrl.getInstance().getAlias(aardvarkID);
		Contact contact = ContactCtrl.getInstance().getContact(aardvarkID);

		// Subscribes as listener for data change in model
		ComBus.subscribe(this);

		// If chat partner is contact, add assigned nickname to alias label
		TextView aliasLabel = (TextView) findViewById(R.id.userNameLable);
		if (contact != null) {
			aliasLabel.setText(alias + " (" + contact.getNickname() + ")");
		} else {
			aliasLabel.setText(alias);
		}

		drawMessages();

		// Make send button send messages when clicked
		Button sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText messageField = (EditText) findViewById(R.id.intputText);
				String message = messageField.getText().toString();
				if (message.length() > 0) {
					Chat chat = ChatCtrl.getInstance().getChat(aardvarkID);
					ChatCtrl.getInstance().sendMessage(chat, message);
					messageField.setText("");
				}
			}
		});

		// If user is blocked, disable send button
		if (UserCtrl.getInstance().isUserBlocked(aardvarkID)) {
			setBlockedEnabled(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		visible = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		visible = false;
	}

	/**
	 * Draws chat messages on screen.
	 */
	private void drawMessages() {
		// Clear existing messages
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.messageLayout);
		ll.removeAllViews();

		try {
			// Add each message in chat.
			List<ChatMessage> messages = ChatCtrl.getInstance().getChatMessages(aardvarkID);
			for (ChatMessage chatMessage : messages) {
				View item;
				if (chatMessage.getUser().getAardvarkID().equals(aardvarkID)) {
					item = inflater.inflate(R.layout.bubbleleftpanel, null);
				} else {
					item = inflater.inflate(R.layout.bubblerightpanel, null);
				}
				TextView tx = (TextView) item.findViewById(R.id.message);
				tx.setText(chatMessage.getMessage() + "\n \n"
						+ chatMessage.getTimeStamp().format("%F %T"));
				ll.addView(item, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} catch (NullPointerException e) {
			Log.i("INFO", "Could not load chat! " + e.getMessage());
		}

		// Scrolls down to latest message.
		ScrollView sv = (ScrollView) this.findViewById(R.id.scrollViewChat);
		sv.smoothScrollTo(0, 214748364);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (ContactCtrl.getInstance().isContact(aardvarkID)) {
			// Hide Add to contacts menu item if user is already contact.
			MenuItem item = menu.getItem(1);
			item.setVisible(false);
		}
		if (UserCtrl.getInstance().isUserBlocked(aardvarkID)) {
			// Hide Block and show Unblock if user is already blocked.
			MenuItem item = menu.getItem(2);
			item.setVisible(false);
			item = menu.getItem(3);
			item.setVisible(true);
		} else {
			// Otherwise, do the opposite
			MenuItem item = menu.getItem(2);
			item.setVisible(true);
			item = menu.getItem(3);
			item.setVisible(false);
		}

		return super.onPrepareOptionsMenu(menu);
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
			// Add contact was clicked, opening Set nickname dialog.
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getString(R.string.addContactTitleChatView));
			alert.setMessage(getString(R.string.addContactTextChatView));

			// Set an EditText view to get user input.
			final EditText input = new EditText(this);
			input.setText(alias);
			alert.setView(input);

			// If OK clicked, add contact with inputed nickname.
			alert.setPositiveButton(getString(R.string.addContactOKChatView),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String value = input.getText().toString();
							ContactCtrl.getInstance().addContact(value, aardvarkID);
						}
					});

			// If cancel clicked, do nothing.
			alert.setNegativeButton(getString(R.string.addContactCancelChatView),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Cancelled
						}
					});

			alert.show();
			break;
		case R.id.block:
			// Block user clicked, blocks user.
			UserCtrl.getInstance().blockUser(aardvarkID);
			break;
		case R.id.unblock:
			// Unblock user clicked, unblocks user.
			UserCtrl.getInstance().unblockUser(aardvarkID);
			break;
		case R.id.close:
			// Close chat clicked, closes chat.
			Chat currentChat = ChatCtrl.getInstance().getChat(aardvarkID);
			ChatCtrl.getInstance().closeChat(currentChat);
			break;
		}
		return true;
	}

	@Override
	public void notifyEvent(String stateChange, Object object) {
		if (stateChange.equals(StateChanges.NEW_MESSAGE_IN_CHAT.toString())) {
			// New message arrived, update chat view.
			// If view is currently shown, set messages as read.
			Chat chat = (Chat) object;
			if (visible && chat.getRecipient().getAardvarkID().equals(aardvarkID)) {
				chat.clearUnreadMessages();
			}
			drawMessages();
		} else if (stateChange.equals(StateChanges.CONTACT_ADDED.toString())) {
			// Contact added, notifies user.
			Toast.makeText(getApplicationContext(),
					getString(R.string.contactAddedNotificationChatView), Toast.LENGTH_SHORT)
					.show();
		} else if (stateChange.equals(StateChanges.USER_BLOCKED.toString())) {
			// A user was blocked. If user is current chat partner,
			// disable send button.
			User blockedUser = (User) object;
			if (blockedUser.getAardvarkID().equals(aardvarkID)) {
				setBlockedEnabled(true);
			}
		} else if (stateChange.equals(StateChanges.USER_UNBLOCKED.toString())) {
			// A user was unblocked. If user is current chat partner,
			// enable send button.
			User unblockedUser = (User) object;
			if (unblockedUser.getAardvarkID().equals(aardvarkID)) {
				setBlockedEnabled(false);
			}
		} else if (stateChange.equals(StateChanges.CHAT_CLOSED.toString())) {
			// Chat was closed, closes this view.
			this.finish();
		}
	}

	/**
	 * Sets the send message button as disabled. Used when current user was
	 * blocked.
	 * 
	 * @param enabled
	 *            true if user was blocked, false if unblocked.
	 */
	private void setBlockedEnabled(boolean enabled) {
		Button sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setEnabled(!enabled);
	}
}
