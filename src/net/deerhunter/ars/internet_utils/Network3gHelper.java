package net.deerhunter.ars.internet_utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Class that helps to work with 3G module.
 * 
 * @author DeerHunter
 */
public class Network3gHelper {
	private Network3gHelper() {}

	/**
	 * Changes the 3G module state.
	 * 
	 * @param context Context of the application component
	 * @param enabled New state of the 3G module
	 * @throws Exception if the device doesn't have the access to the 3G module
	 */
	public static void change3gState(Context context, boolean enabled) throws Exception {
		if (is3GEnabled(context) == enabled)
			return;
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class<?> connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
		final Field connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
		connectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = connectivityManagerField.get(connectivityManager);
		final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled",
				Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	/**
	 * Returns the state of the 3G module.
	 * 
	 * @param context Context of the application component
	 * @return State of the 3G module
	 * @throws Exception if the device doesn't have the access to the 3G module
	 */
	public static boolean is3GEnabled(Context context) throws Exception {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class<?> connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
		final Field connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
		connectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = connectivityManagerField.get(connectivityManager);
		final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		final Method getMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("getMobileDataEnabled");
		getMobileDataEnabledMethod.setAccessible(true);

		return (Boolean) getMobileDataEnabledMethod.invoke(iConnectivityManager);
	}
}
