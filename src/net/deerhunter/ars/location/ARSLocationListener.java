package net.deerhunter.ars.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class ARSLocationListener implements LocationListener {

	private static volatile ARSLocationListener instance;

	private Location bestLocation = null;

	public static ARSLocationListener getInstance() {
		ARSLocationListener localInstance = instance;
		if (localInstance == null) {
			synchronized (ARSLocationListener.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ARSLocationListener();
				}
			}
		}
		return localInstance;
	}
	// 3 minutes
	public static final long START_LOCATION_LISTENING_TIME = 1000 * 60 * 3; 
	// 1 hour
	public static final long LOCATION_LISTENING_INTERVAL = 1000 * 60 * 60; 
	// 3 minutes
	public static final long UPDATE_PERIOD = 1000 * 60 * 3; 

	@Override
	public void onLocationChanged(Location location) {
		Log.e("new location", "latitude = " + location.getLatitude() + ", longitude = " + location.getLongitude());
		if (isBetterLocation(location, bestLocation))
			bestLocation = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.e("provider disabled", provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.e("provider enabled", provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.e("onStatusChanged", provider + "  " + status + "  ");
	}

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	private boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > UPDATE_PERIOD;
		boolean isSignificantlyOlder = timeDelta < -UPDATE_PERIOD;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public Location getBestLocation() {
		return bestLocation;
	}
}
