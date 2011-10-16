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

package edu.chalmers.aardvark.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

/**
 * ComBus is used for managing the event listeners, and for any class to send
 * events when it has experienced a change in state.
 * 
 * All listeners must extend the class edu.chalmers.aardvark.util.EventListener.
 */
public class ComBus {
	/** List of all event listeners subscribed to events */
	private static List<EventListener> listeners = new ArrayList<EventListener>();

	/**
	 * Subscribe the given EventListener to receive events when being notified
	 * by the notifyListeners method.
	 * 
	 * @param listener
	 *            the EventListener to subscribe.
	 */
	public static void subscribe(EventListener listener) {
		listeners.add(listener);
	}

	/**
	 * Unsubscribe the given EventListener from receiving events. If it is not
	 * subscribed, nothing will be done.
	 * 
	 * @param listener
	 *            the EventListener to unsubscribe.
	 */
	public static void unsubscribe(EventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Send event to all listeners using a state change code from the
	 * edu.chalmers.aardvark.util.StateChanges enum and an optional object,
	 * typically the object which experienced the change of state.
	 * 
	 * @param stateChange
	 *            state change code from StateChanges enum.
	 * @param object
	 *            the object that changed its state.
	 */
	public static void notifyListeners(String stateChange, Object object) {
		Log.i("EVENT", "Sending event " + stateChange);
		// Send event to all listeners.
		for (EventListener l : listeners) {
			// If listener is GUI activity, send in GUI thread since
			// Android will not execute GUI code in another thread.
			// Otherwise send in current thread.
			if (l instanceof Activity) {
				Activity target = (Activity) l;
				final EventListener fL = l;
				final String fStateChange = stateChange;
				final Object fObject = object;
				target.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						fL.notifyEvent(fStateChange, fObject);
					}
				});
			} else {
				l.notifyEvent(stateChange, object);
			}
		}
	}
}
