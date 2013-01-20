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

package net.deerhunter.ars.protocol.packets;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import net.deerhunter.ars.application.ArsApplication;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * This class is a wrapper. Each packet sent through the network must be wrapped
 * using the object of this class.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class MainPacket extends BasePacket{
	private int packetLength;
	private int dataType;
	private String imei;
	private int imeiSize;
	private byte[] data;
	private byte[] packet;
	private static int HEADER_LENGTH = 12;

	/**
	 * Constructs a main packet, that can be sent to the server.
	 * 
	 * @param dataType Type of the data in this main packet (constant from
	 *            DataType class)
	 * @param data Data, that will be sent to the server
	 */
	public MainPacket(int dataType, byte[] data) {
		this.dataType = dataType;
		this.data = data;
		TelephonyManager telephony = (TelephonyManager) ArsApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		this.imei = telephony.getDeviceId();
		this.imeiSize = calculateStringSize(imei, Charset.forName("UTF8"));
		this.packetLength = data.length + HEADER_LENGTH + imeiSize;
		generatePacket();
	}

	/**
	 * Method generates a binary representation of the main packet.
	 */
	private void generatePacket() {
		ByteBuffer buffer = ByteBuffer.allocate(packetLength);
		buffer.putInt(packetLength);
		buffer.putInt(dataType);
		buffer.putInt(imeiSize);
		buffer.put(imei.getBytes());
		buffer.put(data);
		packet = buffer.array();
	}

	/**
	 * Returns length of the main packet.
	 * 
	 * @return Packet length
	 */
	public int getPacketLength() {
		return packetLength;
	}

	/**
	 * Returns type of the data that are held in data array (constant from
	 * DataType class).
	 * 
	 * @return Type of the data
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Returns data that are held in this main packet.
	 * 
	 * @return Data that are held in this main packet
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Returns a binary representation of this main packet.
	 * 
	 * @return Binary representation of this main packet
	 */
	public byte[] getPacket() {
		return packet;
	}
}
