package edu.chalmers.aardvark;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AardvarkApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
	super.onCreate();
	context = getContext();
	Log.i("AWESOME", "CONTEXT WAS GLOBALIZED");
    }

    public static Context getContext() {
	return context;
    }
}
