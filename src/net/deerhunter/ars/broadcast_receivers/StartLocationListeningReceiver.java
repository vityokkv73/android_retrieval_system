package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.gps.GPSHelper;
import net.deerhunter.ars.internet_utils.WifiHelper;
import net.deerhunter.ars.location.ARSLocationListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;

public class StartLocationListeningReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		if (WifiHelper.isWifiEnabled(context)) {
			prefEditor.putBoolean("isWifiEnabledByUser", true);
			prefEditor.commit();
		} else {
			WifiHelper.changeWifiState(context, true);
		}

		if (GPSHelper.isGPSEnabled(context)) {
			prefEditor.putBoolean("isGPSEnabledByUser", true);
			prefEditor.commit();
		} else {
			GPSHelper.changeGPSState(context, true);
		}
		startLocationListening(context);
	}

	private void startLocationListening(Context context) {
		ARSLocationListener locationListener = ARSLocationListener.getInstance();
		LocationManager networkLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		networkLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		networkLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
}
