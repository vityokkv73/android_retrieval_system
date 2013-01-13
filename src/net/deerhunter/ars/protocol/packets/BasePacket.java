package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Abstract class that contains useful methods to construct binary
 * representation for all packets transfered through the network.
 * 
 * @author DeerHunter
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
		// array that holds a binary representation of java base classes
		byte[] tmpArray;
		// array that holds a size of data
		ByteBuffer sizeArray = ByteBuffer.allocate(4);

		tmpArray = string.getBytes(charset);
		sizeArray.putInt(tmpArray.length);
		outStream.write(sizeArray.array());
		outStream.write(tmpArray);
	}

	/**
	 * Method writes a double value to the output string as hex string.
	 * 
	 * @param outStream Output stream the string will be written to
	 * @param value Value hat will be written to the output stream
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void writeDoubleHexStringToArray(ByteArrayOutputStream outStream, double value) throws IOException {
		String stringValue = Long.toHexString(Double.doubleToLongBits(value));
		String fullDoubleString = hexDoublePattern.substring(0, 16 - stringValue.length()) + stringValue;
		outStream.write(fullDoubleString.getBytes());
	}

	/**
	 * Method writes a float value to the output string as hex string.
	 * 
	 * @param outStream Output stream the string will be written to
	 * @param value Value hat will be written to the output stream
	 * @throws IOException if exception occurs in the output stream
	 */
	protected void writeFloatHexStringToArray(ByteArrayOutputStream outStream, float value) throws IOException {
		String stringValue = Integer.toHexString(Float.floatToIntBits(value));
		String fullFloatString = hexFloatPattern.substring(0, 8 - stringValue.length()) + stringValue;
		outStream.write(fullFloatString.getBytes());
	}
}
