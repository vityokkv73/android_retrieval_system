package net.deerhunter.ars.internet_utils;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiHelper {
	private WifiHelper() {
	}

	public static void changeWifiState(Context context, boolean state) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled() == state)
			return;
		wifiManager.setWifiEnabled(state);
	}
	
	public static boolean isWifiEnabled(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled())
			return true;
		return false;
	}
}
