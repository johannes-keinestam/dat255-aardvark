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

import android.util.Log;

/**
 * Controller class which handles all encryption related functionality.
 * 
 * Note: not implemented.
 */
public class EncryptionCtrl {
	/** Static variable for singleton instance */
	private static EncryptionCtrl instance;

	/**
	 * Private constructor.
	 */
	private EncryptionCtrl() {
		Log.i("CLASS", this.toString() + " STARTED");
	}

	/**
	 * Returns the singleton instance of EncryptionCtrl.
	 * 
	 * @return singleton instance.
	 */
	public static EncryptionCtrl getInstance() {
		if (instance == null) {
			instance = new EncryptionCtrl();
		}
		return instance;
	}
}
