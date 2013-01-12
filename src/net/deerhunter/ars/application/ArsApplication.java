package net.deerhunter.ars.application;

import android.app.Application;
import android.content.SharedPreferences;

public class ArsApplication extends Application{
	private static volatile ArsApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	/**
	 * Returns an application instance.
	 * @return An application instance
	 */
	public static ArsApplication getInstance(){
		return instance;
	}
	
	/**
	 * Returns the application preferences.
	 * @return Application preferences
	 */
	public SharedPreferences getAppPrefs(){
		return getSharedPreferences(getPackageName(), MODE_PRIVATE);
	}

}
