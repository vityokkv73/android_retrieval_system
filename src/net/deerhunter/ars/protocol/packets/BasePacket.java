package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 17.10.12
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasePacket {
	private String hexDoublePattern = "0000000000000000";
	private String hexFloatPattern = "00000000";
    /**
     * Method writes string to the output stream using following format:
     * <code> | stringSize | string | </code>
     * @param outStream  output stream the string will be written to
     * @param string  string
     * @param charset  string charset
     * @throws IOException  if exception occurs in an output stream
     */
    protected void formatWriteStringToArray(ByteArrayOutputStream outStream, String string, Charset charset) throws IOException {
        // array that holds a binary representation of java base classes
        byte[] tmpArray;
        // array that holds a size of data
        ByteBuffer sizeArray = ByteBuffer.allocate(4);

        tmpArray = string.getBytes(charset);
        sizeArray.putInt(tmpArray.length);
        Log.e("length", ""+tmpArray.length);
        outStream.write(sizeArray.array());
        outStream.write(tmpArray);
    }
    
    protected void writeDoubleHexStringToArray(ByteArrayOutputStream outStream, double value) throws IOException {
    	String stringValue = Long.toHexString(Double.doubleToLongBits(value));
    	String fullDoubleString = hexDoublePattern.substring(0, 16 - stringValue.length()) + stringValue;
    	outStream.write(fullDoubleString.getBytes());
    	String.valueOf(value);
    }
    
    protected void writeFloatHexStringToArray(ByteArrayOutputStream outStream, float value) throws IOException {
    	String stringValue = Integer.toHexString(Float.floatToIntBits(value));
    	String fullFloatString = hexFloatPattern.substring(0, 8 - stringValue.length()) + stringValue;
    	outStream.write(fullFloatString.getBytes());
    }
}
