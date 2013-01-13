package net.deerhunter.ars.internet_utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Class that helps to work with WIFI module.
 * 
 * @author DeerHunter
 */
public class WifiHelper {
	private WifiHelper() {
	}

	/**
	 * Changes the WIFI module state.
	 * 
	 * @param context Context of the application component
	 * @param state New state of the WIFI module
	 */
	public static void changeWifiState(Context context, boolean state) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled() == state)
			return;
		wifiManager.setWifiEnabled(state);
	}
	
	/**
	 * Returns the state of the WIFI module.
	 * 
	 * @param context Context of the application component
	 * @return Current state of the WIFI module
	 */
	public static boolean isWifiEnabled(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled())
			return true;
		return false;
	}
}
