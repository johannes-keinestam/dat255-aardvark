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

import android.text.format.Time;
import android.util.Log;

/**
 * Model class representing a message displayed in a chat.
 */
public class ChatMessage {
	/** The text message body */
	private String message;
	/** The user that sent the message */
	private User user;
	/**
	 * Boolean indicating whether the message was received from a remote user,
	 * or was sent by the local user
	 */
	private boolean isRemote;
	/** The time the message was sent or received */
	private Time timeStamp;

	/**
	 * Constructor initializing the message.
	 * 
	 * @param msg
	 *            the message text body.
	 * @param user
	 *            the user who sent the message.
	 * @param remote
	 *            was the message received (true) or sent (false).
	 * @param time
	 *            the time the message was received or sent.
	 */
	public ChatMessage(String msg, User user, boolean remote, Time time) {
		message = msg;
		this.user = user;
		isRemote = remote;
		timeStamp = time;
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Gets the text message body.
	 * 
	 * @return String with the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the user who sent the message.
	 * 
	 * @return User object representing sender of message.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Returns whether the message was received from a remote user, or was sent
	 * by the local user
	 * 
	 * @return true if user was received, false if sent.
	 */
	public boolean isRemote() {
		return isRemote;
	}

	/**
	 * Gets the time the message was received or sent.
	 * 
	 * @return Time object.
	 */
	public Time getTimeStamp() {
		return timeStamp;
	}
}
