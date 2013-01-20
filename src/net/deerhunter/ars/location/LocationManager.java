/*
 * This file is part of Android retrieval system project.
 * 
 * Android retrieval system is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. 
 * 
 * Android retrieval system is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Android retrieval system. If not, see <http://www.gnu.org/licenses/>.
 */

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

/**
 * This class is used to start and stop listening of the location.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class LocationManager {
	private static volatile LocationManager instance;

	private LocationManager() {
	}

	/**
	 * Gets the instance of the <code>LocationManager</code> class.
	 * 
	 * @return The instance of the <code>LocationManager</code> class
	 */
	public static LocationManager getInstance() {
		LocationManager localInstance = instance;
		if (localInstance == null) {
			synchronized (LocationManager.class) {
				localInstance = instance;
				if (localInstance == null)
					localInstance = instance = new LocationManager();
			}
		}
		return localInstance;
	}

	/**
	 * Starts a location controller.
	 * 
	 * @param startNow Flag that indicates a necessity to start a location
	 *            controller now
	 */
	public void startLocationController(boolean startNow) {
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		Context context = ArsApplication.getInstance().getApplicationContext();
		int startLocationUpdate = 1000;  // 1 sec to start
		if (!startNow)
			startLocationUpdate = 1000 * prefs.getInt(context.getString(R.string.startLocationUpdate),
					ARSLocationListener.START_LOCATION_LISTENING_TIME);
		int locationListeningInterval = 1000 * prefs.getInt(context.getString(R.string.locationListeningInterval),
				ARSLocationListener.LOCATION_LISTENING_INTERVAL);
		int updateLocationPeriod = 1000 * prefs.getInt(context.getString(R.string.updateLocationPeriod),
				ARSLocationListener.UPDATE_PERIOD);

		AlarmManager startLocationListener = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent startLocationListeningIntent = new Intent(context, StartLocationListeningReceiver.class);
		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, startLocationListeningIntent, 0);
		startLocationListener.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startLocationUpdate,
				locationListeningInterval, pendingIntent1);

		AlarmManager stopLocationListener = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent stoplocationListeningIntent = new Intent(context, StopLocationListeningReceiver.class);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, stoplocationListeningIntent, 0);
		stopLocationListener.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + startLocationUpdate
				+ updateLocationPeriod, locationListeningInterval, pendingIntent2);
	}

	/**
	 * Stops all the alarm managers that start and stop the location listeners
	 */
	public void stopAllLocationAlarmManager() {
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
