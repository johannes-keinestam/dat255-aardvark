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

package edu.chalmers.aardvark.services;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.chalmers.aardvark.gui.MainViewActivity;

/**
 * Service class for sending notifications to the user. These notifications are
 * shown in the Android status bar and notification window.
 * 
 * This class is a service to make Android keep it alive even when the
 * application might not need it. It also grants access to needed functionality
 * for sending these notifications.
 */
public class Notifier extends Service {
	/** The notification manager of the Android OS */
	NotificationManager notifier;

	@Override
	public void onCreate() {
		Log.i("INFO", "Creating notifier...");

		// Gets the notification manager from Android OS.
		notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Method called when an intent was received through startService
		Log.i("INFO", "Notifying user...");

		// Creates the notification's text.
		String notificationTickerText = getString(edu.chalmers.aardvark.R.string.notificationTickerText);
		String notificationTitle = getString(edu.chalmers.aardvark.R.string.notificationTitle);
		String notificationDetails = getString(edu.chalmers.aardvark.R.string.notificationDetails);
		Context context = getApplicationContext();

		// Creates the intent to be executed when notification is clicked.
		Intent openIntent = new Intent(this, MainViewActivity.class);
		openIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, openIntent, 0);

		// Associate text and icon to the notification.
		Notification notification = new Notification(R.drawable.sym_def_app_icon,
				notificationTickerText, System.currentTimeMillis());
		notification.setLatestEventInfo(context, notificationTitle, notificationDetails,
				notificationIntent);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		// Notify user.
		notifier.notify(1, notification);

		// If killed, restart when new intent received.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Service not bound.
		return null;
	}

}