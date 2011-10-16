/**
 * Copyright 2011 Fredrik Hidstrand, Johannes Keinestam, Magnus Sj�qvist, Fredrik Thander
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

/**
 * Interface implemented by classes who wants to listen for changes in state.
 */
public interface EventListener {

	/**
	 * Called when an event is received, typically describing a change in state.
	 * 
	 * To use, check if incoming state change is equal to one you want to listen
	 * to, and then perform appropriate actions.
	 * 
	 * @param stateChange
	 *            a String corresponding to a state change code from
	 *            StateChanges enum. Used for identifying what happened, and how
	 *            to react to it.
	 * @param object
	 *            typically the object that changed it state, or the new state.
	 */
	public void notifyEvent(String stateChange, Object object);

}
