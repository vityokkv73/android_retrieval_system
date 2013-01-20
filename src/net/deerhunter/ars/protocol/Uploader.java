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
 * @author DeerHunter (vityokkv73@gmail.com)
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
		httpUrlConnection.setReadTimeout(10000);
		httpUrlConnection.setConnectTimeout(10000);
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