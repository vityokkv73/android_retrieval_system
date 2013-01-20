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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * This class contains all the information and methods needed to send the SMSes
 * to the server.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class SMSPacket extends BasePacket {
	private String sender;
	private String recipient;
	private String senderPhoneNumber;
	private String recipientPhoneNumber;
	private long time;
	private String text;
	private byte[] binaryPacket;

	/**
	 * Constructs a new SMSPacket using all necessary parameters.
	 * 
	 * @param sender Sender of this SMS
	 * @param recipient Recipient of this SMS
	 * @param senderPhoneNumber Phone number of a sender
	 * @param recipientPhoneNumber Phone number of a recipient
	 * @param time SMS sending or delivery time
	 * @param text SMS text
	 */
	public SMSPacket(String sender, String recipient, String senderPhoneNumber, String recipientPhoneNumber, long time,
			String text) {
		this.sender = sender;
		this.recipient = recipient;
		this.senderPhoneNumber = senderPhoneNumber;
		this.recipientPhoneNumber = recipientPhoneNumber;
		this.time = time;
		this.text = text;
		generateBinaryPacket();
	}

	/**
	 * Method generate a binary representation of a SMS packet.
	 */
	private void generateBinaryPacket() {
		ByteArrayOutputStream outputArray = new ByteArrayOutputStream(300);
		try {
			Charset UTF8Charset = Charset.forName("UTF-8");

			formatWriteStringToArray(outputArray, sender, UTF8Charset);
			formatWriteStringToArray(outputArray, recipient, UTF8Charset);
			formatWriteStringToArray(outputArray, senderPhoneNumber, UTF8Charset);
			formatWriteStringToArray(outputArray, recipientPhoneNumber, UTF8Charset);

			// write a time to the output stream
			ByteBuffer longArray = ByteBuffer.allocate(8);
			longArray.putLong(time);
			outputArray.write(longArray.array());

			formatWriteStringToArray(outputArray, text, UTF8Charset);

			binaryPacket = outputArray.toByteArray();
		} catch (IOException e) {
		}
	}

	/**
	 * Method returns a sender name of this SMS.
	 * 
	 * @return Sender of SMS
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Method returns a recipient name of this SMS.
	 * 
	 * @return Recipient of SMS
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * Method returns a phone number of a sender.
	 * 
	 * @return Phone number of a sender
	 */
	public String getSenderPhoneNumber() {
		return senderPhoneNumber;
	}

	/**
	 * Method returns a phone number of a recipient.
	 * 
	 * @return Phone number of a recipient
	 */
	public String getRecipientPhoneNumber() {
		return recipientPhoneNumber;
	}

	/**
	 * Method returns a time when this SMS was delivered (sent).
	 * 
	 * @return Time when SMS was delivered (sent)
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Method returns a text of SMS.
	 * 
	 * @return Text of SMS
	 */
	public String getText() {
		return text;
	}

	/**
	 * Method returns a binary representation of a SMS packet.
	 * 
	 * @return Binary representation of a SMS packet
	 */
	public byte[] getBinaryPacket() {
		return binaryPacket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(300);
		builder.append("SMSPacket { ");
		builder.append("sender = " + sender);
		builder.append(", recipient = " + recipient);
		builder.append(", senderPhoneNumber = " + senderPhoneNumber);
		builder.append(", recipientPhoneNumber = " + recipientPhoneNumber);
		builder.append(", time = " + new Date(time));
		builder.append(", body = " + text);
		builder.append(" }");
		return builder.toString();
	}
}
