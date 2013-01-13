package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.gps.GPSHelper;
import net.deerhunter.ars.internet_utils.WifiHelper;
import net.deerhunter.ars.location.ARSLocationListener;
import net.deerhunter.ars.providers.ActivityContract;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;

/**
 * This class is used to stop listening the location and save the best location
 * into the database.
 * 
 * @author DeerHunter
 */
public class StopLocationListeningReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		stopLocationListening(context);
		saveBestLocationInDB(context);
		disableGpsAndWifiIfNeeded(context);
	}

	/**
	 * Disables GPS and WIFI modules after the listening a location if they were
	 * not enabled by the user.
	 * 
	 * @param context Context of the application component
	 */
	private void disableGpsAndWifiIfNeeded(Context context) {
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		boolean isWifiEnabledByUser = prefs.getBoolean(context.getString(R.string.isWifiEnabledByUser), false);
		boolean isGPSEnabledByUser = prefs.getBoolean(context.getString(R.string.isGPSEnabledByUser), false);
		if (!isWifiEnabledByUser)
			WifiHelper.changeWifiState(context, false);
		if (!isGPSEnabledByUser)
			GPSHelper.changeGPSState(context, false);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(context.getString(R.string.isWifiEnabledByUser), false);
		prefEditor.putBoolean(context.getString(R.string.isGPSEnabledByUser), false);
		prefEditor.commit();
	}

	/**
	 * Saves the best location into the database.
	 * 
	 * @param context Context of the application component
	 */
	private void saveBestLocationInDB(Context context) {
		ARSLocationListener locationListener = ARSLocationListener.getInstance();
		Location bestLocation = locationListener.getBestLocation();
		double latitude = -10000.0;
		double longitude = -10000.0;
		double altitude = -10000.0;
		float accuracy = -1.0f;
		String provider = "Unknown";
		long time = System.currentTimeMillis();
		if (bestLocation != null) {
			latitude = bestLocation.getLatitude();
			longitude = bestLocation.getLongitude();
			altitude = bestLocation.getAltitude();
			accuracy = bestLocation.getAccuracy();
			provider = bestLocation.getProvider();
			time = bestLocation.getTime();
		}

		ContentValues newLocation = new ContentValues();
		newLocation.put(ActivityContract.Locations.LATITUDE, latitude);
		newLocation.put(ActivityContract.Locations.LONGITUDE, longitude);
		newLocation.put(ActivityContract.Locations.ALTITUDE, altitude);
		newLocation.put(ActivityContract.Locations.ACCURACY, accuracy);
		newLocation.put(ActivityContract.Locations.PROVIDER, provider);
		newLocation.put(ActivityContract.Locations.TIME, time);
		context.getContentResolver().insert(ActivityContract.Locations.CONTENT_URI, newLocation);
	}

	/**
	 * Stops listening of the location.
	 * 
	 * @param context Context of the application component
	 */
	private void stopLocationListening(Context context) {
		ARSLocationListener locationListener = ARSLocationListener.getInstance();
		LocationManager networkLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		networkLocationManager.removeUpdates(locationListener);
	}
}
