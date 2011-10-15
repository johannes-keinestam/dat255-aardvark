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

package edu.chalmers.aardvark.ctrl;

import java.util.List;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import edu.chalmers.aardvark.AardvarkApp;
import edu.chalmers.aardvark.model.ActiveChatContainer;
import edu.chalmers.aardvark.model.Chat;
import edu.chalmers.aardvark.model.ChatMessage;
import edu.chalmers.aardvark.model.LocalUser;
import edu.chalmers.aardvark.model.User;
import edu.chalmers.aardvark.services.MessageSender;
import edu.chalmers.aardvark.services.Notifier;

/**
 * Controller class which handles all chat related functionality.
 */
public class ChatCtrl {
	/** Static variable for singleton instance */
	private static ChatCtrl instance;
	/** ActiveChatContainer, data storage of all chat objects */
	private ActiveChatContainer chatContainer;

	/**
	 * Private constructor.
	 */
	private ChatCtrl() {
		chatContainer = new ActiveChatContainer();
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of ChatCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static ChatCtrl getInstance() {
		if (instance == null) {
			instance = new ChatCtrl();
		}
		return instance;
	}

	/**
	 * Gets the chat object associated with the given AardvarkID.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return the chat associated with the given AardvarkID, or null if none
	 *         found.
	 */
	public Chat getChat(String aardvarkID) {
		return chatContainer.findChatByID(aardvarkID);
	}

	/**
	 * Gets all the chats currently active from ChatContainer.
	 * 
	 * @return list of chat objects.
	 */
	public List<Chat> getChats() {
		return chatContainer.getChats();
	}

	/**
	 * Starts a new chat with a given user.
	 * 
	 * @param user
	 *            the user to start a chat with.
	 */
	public void newChat(User user) {
		Chat chat = new Chat(user);

		chatContainer.addChat(chat);
	}

	/**
	 * Gets all chat messages from a chat associated with given AardvarkID.
	 * 
	 * @param aardvarkID
	 *            the AardvarkID to search for.
	 * @return a list of ChatMessage objects from the found Chat object.
	 */
	public List<ChatMessage> getChatMessages(String aardvarkID) {
		return getChat(aardvarkID).getMessages();
	}

	/**
	 * Sends a chat message to the server, using the threaded service
	 * MessageSender.
	 * 
	 * @param chat
	 *            the chat in which the message was received.
	 * @param message
	 *            the text message to send.
	 */
	public void sendMessage(Chat chat, String message) {
		// Creates intent for starting MessageSender service
		Context context = AardvarkApp.getContext();
		Intent intent = new Intent(context, MessageSender.class);
		intent.putExtra("msg", message);
		intent.putExtra("to", chat.getRecipient().getAardvarkID());

		// Creating local ChatMessage object to put into chat
		Time time = new Time(Time.getCurrentTimezone());
		time.setToNow();
		ChatMessage chatMessage;
		chatMessage = new ChatMessage(message, LocalUser.getLocalUser(), false, time);

		// Adding message to chat
		chat.addMessage(chatMessage);

		// Sending message to server with MessageSender
		context.startService(intent);
	}

	/**
	 * Used for receiving messages packets from the server. Interprets the
	 * packet and creates an instance of it in an existing or a new chat.
	 * 
	 * @param packet
	 *            the packet to process (typically a Message object).
	 */
	public void receiveMessage(Packet packet) {
		String aardvarkID = packet.getFrom();

		// If sender is on block list, do nothing.
		if (UserCtrl.getInstance().isUserBlocked(aardvarkID)) {
			return;
		}

		// If chat with sender does not exist already, create it.
		Chat chat = ChatCtrl.getInstance().getChat(aardvarkID);
		if (chat == null) {
			String alias = ServerHandlerCtrl.getInstance().getAlias(packet.getFrom());
			ChatCtrl.getInstance().newChat(new User(alias, packet.getFrom()));
			chat = ChatCtrl.getInstance().getChat(packet.getFrom());
		}

		// Create local ChatMessage and add it to chat with sender
		ChatMessage chatMessage;
		Time time = new Time(Time.getCurrentTimezone());
		time.setToNow();
		Message message = (Message) packet;
		chatMessage = new ChatMessage(message.getBody(), chat.getRecipient(), true, time);
		chat.addMessage(chatMessage);

		// Notifies user of pending message with Notifier service
		AardvarkApp.getContext().startService(new Intent(AardvarkApp.getContext(), Notifier.class));
	}

	/**
	 * Removes a chat from the list of active chats. Typically used when closing
	 * chats in the GUI.
	 * 
	 * @param chat
	 *            the chat object to remove.
	 */
	public void closeChat(Chat chat) {
		chatContainer.removeChat(chat);
	}

	/**
	 * Removes all chats from the list of active chats. Used at log out.
	 */
	public void closeChats() {
		chatContainer.getChats().clear();
	}
}
