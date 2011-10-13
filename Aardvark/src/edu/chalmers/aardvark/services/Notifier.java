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
import edu.chalmers.aardvark.gui.ChatViewActivity;
import edu.chalmers.aardvark.gui.MainViewActivity;

public class Notifier extends Service {
    NotificationManager notifier;
    
    @Override
    public void onCreate() {
        Log.i("INFO", "Creating notifier...");
        notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("INFO", "Creating notification...");
        String notificationTickerText = getString(edu.chalmers.aardvark.R.string.notificationTickerText);
        String notificationTitle = getString(edu.chalmers.aardvark.R.string.notificationTitle);
        String notificationDetails = getString(edu.chalmers.aardvark.R.string.notificationDetails);
        Context context = getApplicationContext();
        
        Intent openIntent = new Intent(this, MainViewActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, openIntent, 0);

        Notification notification = new Notification(R.drawable.sym_def_app_icon, notificationTickerText, System.currentTimeMillis());
        notification.setLatestEventInfo(context, notificationTitle, notificationDetails, notificationIntent);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notifier.notify(1, notification);
        Log.i("INFO", "NOTIFIED");
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}