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

package net.deerhunter.ars.internet_utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Class that helps to work with WIFI module.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
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
		return wifiManager.isWifiEnabled();
	}
}
