package edu.chalmers.aardvark;

import edu.chalmers.aardvark.ctrl.SystemCtrl;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AardvarkApp extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		Log.i("CLASS", this.toString() + " STARTED");

		SystemCtrl.getCtrl().performStartUpDuty();
	}

	public static Context getContext() {
		return context;
	}
}
