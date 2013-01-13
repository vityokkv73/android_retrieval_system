package net.deerhunter.ars.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.protocol.packets.MainPacket;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is used to send packets to the server.
 * 
 * @author DeerHunter
 */
public class Uploader {
	private Uploader() {
	}

	/**
	 * Sends packets to the server.
	 * 
	 * @param packet Packet that must be sent
	 * @param context Context of the application component
	 * @return Flag that indicated success of packet delivering
	 * @throws IOException if some problems with network occurred
	 */
	public static boolean sendPacket(MainPacket packet, Context context) throws IOException {
		BufferedInputStream fis = new BufferedInputStream(new ByteArrayInputStream(packet.getPacket()));

		SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();

		String serverAddress = prefs.getString(context.getString(R.string.serverAddress),
				context.getString(R.string.defaultServerAddress));

		HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(serverAddress + System.currentTimeMillis())
				.openConnection();
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setRequestMethod("POST");
		OutputStream os = httpUrlConnection.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(os);

		int totalByte = packet.getPacketLength();
		int byteTransferred = 0;
		byte[] fileBuffer = new byte[totalByte];
		do {
			int readDataLength = fis.read(fileBuffer, byteTransferred, totalByte - byteTransferred);
			bos.write(fileBuffer, byteTransferred, readDataLength);

			byteTransferred += readDataLength;
		} while (totalByte != byteTransferred);

		bos.flush();
		bos.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));

		boolean result = false;
		String s = in.readLine();
		if (s != null && s.equals("Done"))
			result = true;

		in.close();
		fis.close();

		return result;
	}
}