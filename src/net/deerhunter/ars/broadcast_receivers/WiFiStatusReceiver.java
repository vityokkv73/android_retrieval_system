package net.deerhunter.ars.broadcast_receivers;

import net.deerhunter.ars.protocol.PacketSenderService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 20.10.12
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public class WiFiStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            sendData(context);
        }
    }

    private void sendData(Context context) {
        ConnectivityManager myConnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo myNetworkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (myNetworkInfo.isConnected()) {
            Intent packetSenderIntent = new Intent(context, PacketSenderService.class);
            context.startService(packetSenderIntent);
        } else {
            //disconnected
        }

    }
}
