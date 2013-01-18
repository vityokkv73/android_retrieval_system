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

		sendData(context, networkInfo.getType());
	}

	/**
	 * Starts the service to send all gathered data to the server.
	 * 
	 * @param context Context of the application component
	 * @param type Type of the connection
	 */
	private void sendData(Context context, int type) {
		ConnectivityManager myConnManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo myNetworkInfo = myConnManager.getNetworkInfo(type);

		if (myNetworkInfo.isConnected()) {
			Intent packetSenderIntent = new Intent(context, PacketSenderService.class);
			packetSenderIntent.putExtra("type", type);
			context.startService(packetSenderIntent);
		} else {
			// disconnected
		}

	}
}
