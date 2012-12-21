package net.deerhunter.ars.internet_utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;

public class Network3gHelper {
	private Network3gHelper() {
	}

	public static void change3gState(Context context, boolean enabled) throws Exception {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
