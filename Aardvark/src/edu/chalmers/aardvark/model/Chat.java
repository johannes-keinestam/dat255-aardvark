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

package edu.chalmers.aardvark.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.chalmers.aardvark.util.ComBus;
import edu.chalmers.aardvark.util.StateChanges;

/**
 * Model class representing a chat session with another user.
 * 
 * Sends events to GUI via ComBus.
 */
public class Chat {
	/** The other part of the chat session, recipient of chat messages */
	private User recipient;
	/** List of chat messages in chat session */
	private List<ChatMessage> chatMessages;
	/** Number of unread messages in chat */
	private int unreadMessages = 0;

	/**
	 * Constructor initializing the chat.
	 * 
	 * @param user
	 *            the User object to associate with the chat session.
	 */
	public Chat(User user) {
		chatMessages = new ArrayList<ChatMessage>();
		recipient = user;
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Adds a message to chat session.
	 * 
	 * Sends NEW_MESSAGE_IN_CHAT StateChange on ComBus when done.
	 * 
	 * @param msg
	 *            ChatMessage object to add to chat.
	 */
	public void addMessage(ChatMessage msg) {
		chatMessages.add(msg);
		unreadMessages++;
		ComBus.notifyListeners(StateChanges.NEW_MESSAGE_IN_CHAT.toString(), this);
	}

	/**
	 * Gets the user associated with this chat session, i.e. the recipient of
	 * messages.
	 * 
	 * @return the User object associated with this Chat.
	 */
	public User getRecipient() {
		return recipient;
	}

	/**
	 * Gets the list of messages in the chat session.
	 * 
	 * @return list of ChatMessages in chat.
	 */
	public List<ChatMessage> getMessages() {
		return chatMessages;
	}

	/**
	 * Sets the unread message count to zero.
	 */
	public void clearUnreadMessages() {
		unreadMessages = 0;
	}

	/**
	 * Gets the count of unread messages in this chat session.
	 * 
	 * @return the number of unread messages.
	 */
	public int unreadMessages() {
		return unreadMessages;
	}
}
