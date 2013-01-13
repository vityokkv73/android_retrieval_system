package net.deerhunter.ars.protocol.packets;

import java.nio.ByteBuffer;

/**
 * This class is a wrapper. Each packet sent through the network must be wrapped
 * using the object of this class.
 * 
 * @author DeerHunter
 */
public class MainPacket {
	private int packetLength;
	private int dataType;
	private byte[] data;
	private byte[] packet;
	private static int HEADER_LENGTH = 8;

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
		this.packetLength = data.length + HEADER_LENGTH;
		generatePacket();
	}

	/**
	 * Method generates a binary representation of the main packet.
	 */
	private void generatePacket() {
		ByteBuffer buffer = ByteBuffer.allocate(packetLength);
		buffer.putInt(packetLength);
		buffer.putInt(dataType);
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
