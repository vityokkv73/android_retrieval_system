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
 * @author DeerHunter (vityokkv73@gmail.com)
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
