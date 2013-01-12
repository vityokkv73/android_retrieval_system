package net.deerhunter.ars.location;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.broadcast_receivers.StartLocationListeningReceiver;
import net.deerhunter.ars.broadcast_receivers.StopLocationListeningReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LocationManager {
	private static volatile LocationManager instance;
	
	public static LocationManager getInstance(){
		LocationManager localInstance = instance;
		if(localInstance == null){
			synchronized (LocationManager.class) {
				localInstance = instance;
				if (localInstance == null)
					localInstance = instance = new LocationManager();
			}
		}
		return localInstance;
	}
	
	
	public void startLocationController(boolean startNow) {
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		Context context = ArsApplication.getInstance().getApplicationContext();
		long startLocationUpdate = 1000;  // 1 sec to start
		if (!startNow)
			startLocationUpdate = prefs.getLong(context.getString(R.string.startLocationUpdate), ARSLocationListener.START_LOCATION_LISTENING_TIME);
		long locationListeningInterval = prefs.getLong(context.getString(R.string.locationListeningInterval), ARSLocationListener.LOCATION_LISTENING_INTERVAL);
		long updateLocationPeriod = prefs.getLong(context.getString(R.string.updateLocationPeriod), ARSLocationListener.UPDATE_PERIOD);
		
		AlarmManager startLocationListener = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent startLocationListeningIntent = new Intent(context, StartLocationListeningReceiver.class);
		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, startLocationListeningIntent, 0);
		startLocationListener.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startLocationUpdate, locationListeningInterval, pendingIntent1);
		
		AlarmManager stopLocationListener = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent stoplocationListeningIntent = new Intent(context, StopLocationListeningReceiver.class);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, stoplocationListeningIntent, 0);
		stopLocationListener.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startLocationUpdate + updateLocationPeriod, locationListeningInterval, pendingIntent2);
	}
	
	public void stopAllLocationAlarmManager(){
		Context context = ArsApplication.getInstance().getApplicationContext();
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent startLocationListeningIntent = new Intent(context, StartLocationListeningReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, startLocationListeningIntent, 0);
		Intent stoplocationListeningIntent = new Intent(context, StopLocationListeningReceiver.class);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, stoplocationListeningIntent, 0);
		alarmManager.cancel(pendingIntent);
		alarmManager.cancel(pendingIntent2);
	}
}
