package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.protocol.PacketSenderService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class listens the changes in WIFI state and sends all gathered data to
 * the server.
 * 
 * @author DeerHunter
 */
public class WiFiStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			sendData(context);
		}
	}

	/**
	 * Starts the service to send all gathered data to the server.
	 * 
	 * @param context
	 */
	private void sendData(Context context) {
		ConnectivityManager myConnManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo myNetworkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (myNetworkInfo.isConnected()) {
			Intent packetSenderIntent = new Intent(context, PacketSenderService.class);
			context.startService(packetSenderIntent);
		} else {
			// disconnected
		}

	}
}
