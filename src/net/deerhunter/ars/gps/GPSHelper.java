package net.deerhunter.ars.gps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Class that helps to work with GPS module.
 * 
 * @author DeerHunter
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
