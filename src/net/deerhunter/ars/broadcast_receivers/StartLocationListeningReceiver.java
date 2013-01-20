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

package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.gps.GPSHelper;
import net.deerhunter.ars.internet_utils.WifiHelper;
import net.deerhunter.ars.location.ARSLocationListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;

/**
 * This class is used to start listening the location.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class StartLocationListeningReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
		SharedPreferences.Editor prefEditor = prefs.edit();
		if (WifiHelper.isWifiEnabled(context)) {
			prefEditor.putBoolean(context.getString(R.string.isWifiEnabledByUser), true);
			prefEditor.commit();
		} else {
			WifiHelper.changeWifiState(context, true);
		}

		if (GPSHelper.isGPSEnabled(context)) {
			prefEditor.putBoolean(context.getString(R.string.isGPSEnabledByUser), true);
			prefEditor.commit();
		} else {
			GPSHelper.changeGPSState(context, true);
		}
		startLocationListening(context);
	}

	/**
	 * Starts listening of location.
	 * @param context Context of the application component
	 */
	private void startLocationListening(Context context) {
		ARSLocationListener locationListener = ARSLocationListener.getInstance();
		LocationManager networkLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		networkLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		networkLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
}
