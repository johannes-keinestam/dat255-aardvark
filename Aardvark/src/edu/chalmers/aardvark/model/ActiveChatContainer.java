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
 * Model class used for keeping track of active chats. Internally consists of a
 * list of Chat objects.
 * 
 * Sends events to GUI via ComBus.
 */
public class ActiveChatContainer {
	/** List of Chat objects */
	private List<Chat> activeChats;

	/**
	 * Constructor. Initializes the list.
	 */
	public ActiveChatContainer() {
		activeChats = new ArrayList<Chat>();
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Adds a chat to the list.
	 * 
	 * Sends CHAT_OPENED StateChange on ComBus when done.
	 * 
	 * @param chat
	 *            Chat object to add.
	 */
	public void addChat(Chat chat) {
		activeChats.add(chat);
		ComBus.notifyListeners(StateChanges.CHAT_OPENED.toString(), chat);
	}

	/**
	 * Removes a chat from the list.
	 * 
	 * Sends CHAT_CLOSED StateChange on ComBus when done.
	 * 
	 * @param chat
	 *            Chat object to remove.
	 */
	public void removeChat(Chat chat) {
		activeChats.remove(chat);
		ComBus.notifyListeners(StateChanges.CHAT_CLOSED.toString(), chat);
	}

	/**
	 * Finds a Chat object with the associated user's AardvarkID.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return the associated Chat object if found, null if not.
	 */
	public Chat findChatByID(String aardvarkID) {
		for (Chat c : activeChats) {
			if (c.getRecipient().getAardvarkID().equals(aardvarkID)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Returns a list of all chats.
	 * 
	 * @return the list of currently active chats.
	 */
	public List<Chat> getChats() {
		return activeChats;
	}

}
