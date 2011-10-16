package edu.chalmers.aardvark.util;

/**
 * Enum representing codes for state changes in the application. Used when
 * sending an event with the ComBus by choosing a state change code and
 * converting it to a string.
 * 
 * Typical usage when sending event:
 * 		ComBus.notifyListeners(StateChanges.CONTACT_ADDED.toString(), contact);
 * 
 * Typical usage when receiving event: 
 * 		if (stateChange.equals(StateChanges.CONTACT_ADDED.toString()) 
 * 			// do stuff
 */
public enum StateChanges {
	CONTACT_ADDED, CONTACT_REMOVED, CONTACT_RENAMED, USER_ONLINE, USER_OFFLINE, USER_BLOCKED, USER_UNBLOCKED,

	ENCRYPTION_ENABLED, ENCRYPTION_DISABLED, ENCRYPTION_REQUEST_RECEIVED, ENCRYPTION_REQUEST_SENT,

	NEW_MESSAGE_IN_CHAT, NEW_ENCRYPTED_MESSAGE_IN_CHAT,

	CHAT_OPENED, CHAT_CLOSED,

	SERVER_CONNECTION_LOST, SERVER_CONNECTION_ESTABLISHED, LOGGED_IN, LOGGED_OUT, LOGIN_FAILED, ALIAS_UNAVAILABLE,

	SETTING_UP, STARTING_UP, SHUTTING_DOWN, DONE_SETTING_UP, DONE_STARTING_UP
}
