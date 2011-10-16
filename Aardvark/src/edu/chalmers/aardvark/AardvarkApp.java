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

package edu.chalmers.aardvark;

import edu.chalmers.aardvark.ctrl.SystemCtrl;
import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Application class. Used when the application is started to perform start up
 * procedures with SystemCtrl. Also used for storing a reference to the
 * application context needed when opening some Android components such as
 * services.
 * 
 */
public class AardvarkApp extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("CLASS", this.toString() + " STARTED");

		context = getApplicationContext();

		// Perform start up procedures
		SystemCtrl.getCtrl().performStartUpDuty();
	}

	/**
	 * Gets the application context. Used when opening Android components from
	 * classes not having access to the context from the normal route.
	 * 
	 * @return application context.
	 */
	public static Context getContext() {
		return context;
	}
}
