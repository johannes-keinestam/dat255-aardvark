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

import android.util.Log;

/**
 * Model class representing any user. Typically these objects are not stored,
 * unless they are added to block list or if they are contacts.
 */
public class User {
	/** The alias of the user. The publicly visible identifier of the user. */
	private String alias;
	/** The AardvarkID of the user. Unique identifier of the user. */
	private String aardvarkID;

	/**
	 * Constructor initializing a user.
	 * 
	 * @param alias
	 *            the alias of the user.
	 * @param aardvarkID
	 *            the associated AardvarkID.
	 */
	public User(String alias, String aardvarkID) {
		this.alias = alias;
		this.aardvarkID = aardvarkID;
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Gets the AardvarkID of the user. This is the unique identifier of the
	 * user, used for logging in to the server.
	 * 
	 * @return associated AardvarkID.
	 */
	public String getAardvarkID() {
		return aardvarkID;
	}

	/**
	 * Gets the alias of the user. This is the publicly visible identifier of
	 * the user.
	 * 
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets the alias of the user.
	 * 
	 * @param alias
	 *            new alias to assign.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
