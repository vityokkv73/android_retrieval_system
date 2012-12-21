package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.location.ARSLocationListener;
import net.deerhunter.ars.services.ImageStorageController;
import net.deerhunter.ars.services.SentSMSControllerService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created with IntelliJ IDEA. User: DeerHunter Date: 21.10.12 Time: 14:24 To
 * change this template use File | Settings | File Templates.
 */
public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		startSentSMSController(context);
		startImageStorageController(context);
		stopAllLocationAlarmManager(context);
		startLocationController(context);
	}

	private void startLocationController(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		long startLocationUpdate = prefs.getLong("startLocationUpdate", ARSLocationListener.START_LOCATION_LISTENING_TIME);
		long locationListeningInterval = prefs.getLong("locationListeningInterval", ARSLocationListener.LOCATION_LISTENING_INTERVAL);
		long updateLocationPeriod = prefs.getLong("updateLocationPeriod", ARSLocationListener.UPDATE_PERIOD);
		
		AlarmManager startLocationListener = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent startLocationListeningIntent = new Intent(context, StartLocationListeningReceiver.class);
		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, startLocationListeningIntent, 0);
		startLocationListener.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startLocationUpdate, locationListeningInterval, pendingIntent1);
		
		AlarmManager stopLocationListener = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent stoplocationListeningIntent = new Intent(context, StopLocationListeningReceiver.class);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, stoplocationListeningIntent, 0);
		stopLocationListener.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startLocationUpdate + updateLocationPeriod, locationListeningInterval, pendingIntent2);
	}
	
	private void stopAllLocationAlarmManager(Context context){
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent startLocationListeningIntent = new Intent(context, StartLocationListeningReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, startLocationListeningIntent, 0);
		Intent stoplocationListeningIntent = new Intent(context, StopLocationListeningReceiver.class);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, stoplocationListeningIntent, 0);
		alarmManager.cancel(pendingIntent);
		alarmManager.cancel(pendingIntent2);
	}

	private void startImageStorageController(Context context) {
		Intent imageStorageService = new Intent(context, ImageStorageController.class);
		context.startService(imageStorageService);
	}

	private void startSentSMSController(Context context) {
		Intent sentSMSIntent = new Intent(context, SentSMSControllerService.class);
		context.startService(sentSMSIntent);
	}
}
