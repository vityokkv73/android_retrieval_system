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

package net.deerhunter.ars.gps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Class that helps to work with GPS module.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class GPSHelper {
	private GPSHelper() {
	}

	/**
	 * Changes the GPS module state.
	 * 
	 * @param context Context of the application component
	 * @param state New state of the GPS module
	 */
	public static void changeGPSState(Context context, boolean state) {
		String provider = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps") == state) {
			return; // the GPS is already in the requested state
		}

		final Intent gpsStateIntent = new Intent();
		gpsStateIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		gpsStateIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		gpsStateIntent.setData(Uri.parse("3"));
		context.sendBroadcast(gpsStateIntent);
	}

	/**
	 * Returns the state of the GPS module.
	 * 
	 * @param context Context of the application component
	 * @return Current state of the GPS module
	 */
	public static boolean isGPSEnabled(Context context) {
		String provider = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		return provider.contains("gps");
	}
}
