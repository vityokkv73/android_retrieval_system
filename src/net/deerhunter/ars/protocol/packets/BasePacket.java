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

/**
 * Abstract class that contains useful methods to construct binary
 * representation for all packets transfered through the network.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public abstract class BasePacket {
	private String hexDoublePattern = "0000000000000000";
	private String hexFloatPattern = "00000000";

	/**
	 * Method writes string to the output stream using following format:
	 * <code> | stringSize | string | </code>
	 * 
	 * @param outStream Output stream the string will be written to
	 * @param string String
	 * @param charset String charset
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void formatWriteStringToArray(ByteArrayOutputStream outStream, String string, Charset charset)
			throws IOException {
		// array that holds a binary representation of String object
		byte[] stringArray = string.getBytes(charset);		
		// write size of the string
		writeIntToArray(outStream, stringArray.length);
		// write a string as byte array 
		outStream.write(stringArray);
	}
	
	protected int calculateStringSize(String string, Charset charset){
		byte[] stringArray = string.getBytes(charset);
		return stringArray.length;
	}

	/**
	 * Method writes a double value to the output stream as hex string.
	 * 
	 * @param outStream Output stream the string will be written to
	 * @param value Value that will be written to the output stream
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void writeDoubleHexStringToArray(ByteArrayOutputStream outStream, double value) throws IOException {
		String stringValue = Long.toHexString(Double.doubleToLongBits(value));
		String fullDoubleString = hexDoublePattern.substring(0, 16 - stringValue.length()) + stringValue;
		outStream.write(fullDoubleString.getBytes());
	}

	/**
	 * Method writes a float value to the output stream as hex string.
	 * 
	 * @param outStream Output stream the string will be written to
	 * @param value Value that will be written to the output stream
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void writeFloatHexStringToArray(ByteArrayOutputStream outStream, float value) throws IOException {
		String stringValue = Integer.toHexString(Float.floatToIntBits(value));
		String fullFloatString = hexFloatPattern.substring(0, 8 - stringValue.length()) + stringValue;
		outStream.write(fullFloatString.getBytes());
	}
	
	/**
	 * Method writes a long value to the output stream.
	 * 
	 * @param outStream Output stream the value will be written to
	 * @param value Value that will be written to the output stream
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void writeLongToArray(ByteArrayOutputStream outStream, long value) throws IOException {
		ByteBuffer longArray = ByteBuffer.allocate(8);
		longArray.putLong(value);
		outStream.write(longArray.array());
	}
	
	/**
	 * Method writes an integer value to the output stream.
	 * 
	 * @param outStream Output stream the value will be written to
	 * @param value Value that will be written to the output stream
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void writeIntToArray(ByteArrayOutputStream outStream, int value) throws IOException {
		ByteBuffer intArray = ByteBuffer.allocate(4);
		intArray.putInt(value);
		outStream.write(intArray.array());
	}
}
