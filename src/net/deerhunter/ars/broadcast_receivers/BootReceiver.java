package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.location.LocationManager;
import net.deerhunter.ars.services.ImageStorageController;
import net.deerhunter.ars.services.SentSMSControllerService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA. User: DeerHunter Date: 21.10.12 Time: 14:24 To
 * change this template use File | Settings | File Templates.
 */
public class BootReceiver extends BroadcastReceiver {
	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		LocationManager locationManager = LocationManager.getInstance();
		startSentSMSController();
		startImageStorageController();
		locationManager.stopAllLocationAlarmManager();
		locationManager.startLocationController(false);
	}
	
	private void startImageStorageController() {
		Intent imageStorageService = new Intent(context, ImageStorageController.class);
		context.startService(imageStorageService);
	}

	private void startSentSMSController() {
		Intent sentSMSIntent = new Intent(context, SentSMSControllerService.class);
		context.startService(sentSMSIntent);
	}
}
