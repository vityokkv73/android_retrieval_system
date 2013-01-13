package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import android.util.Log;

/**
 * This class contains all the information and methods needed to send the
 * thumbnails of the images to the server.
 * 
 * @author DeerHunter
 */
public class ThumbnailPacket extends BasePacket {
	private String displayName;
	private String filePath;
	private int storeId;
	private long dateAdded;
	private byte[] image;

	private byte[] binaryPacket;

	/**
	 * Constructs a new ThumbnailPacket using all necessary parameters.
	 * 
	 * @param displayName Name of the image
	 * @param filePath Full path to the image
	 * @param storeId Store ID of the image in the database
	 * @param dateAdded Date when the image was added
	 * @param image Byte array of the image
	 */
	public ThumbnailPacket(String displayName, String filePath, int storeId, long dateAdded, byte[] image) {
		this.displayName = displayName;
		this.filePath = filePath;
		this.storeId = storeId;
		this.dateAdded = dateAdded;
		this.image = image;
		generateBinaryPacket();
	}

	/**
	 * Method generates a binary representation of a thumbnail packet.
	 */
	private void generateBinaryPacket() {
		ByteArrayOutputStream outputArray = new ByteArrayOutputStream(100000);
		try {
			Charset UTF8Charset = Charset.forName("UTF-8");

			formatWriteStringToArray(outputArray, displayName, UTF8Charset);
			formatWriteStringToArray(outputArray, filePath, UTF8Charset);

			ByteBuffer intArray = ByteBuffer.allocate(4);
			intArray.putInt(storeId);
			outputArray.write(intArray.array());

			ByteBuffer longArray = ByteBuffer.allocate(8);
			longArray.putLong(dateAdded);
			outputArray.write(longArray.array());

			Log.e("date added", String.valueOf(dateAdded));

			intArray.clear();
			intArray.putInt(image.length);
			outputArray.write(intArray.array());

			outputArray.write(image);

			binaryPacket = outputArray.toByteArray();
		} catch (IOException e) {
		}
	}

	/**
	 * Method returns name of the image.
	 * 
	 * @return Name of the image
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Method returns a path to the image.
	 * 
	 * @return Path to the image
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Method returns a store ID of the image.
	 * 
	 * @return Store ID of the image
	 */
	public int getStoreId() {
		return storeId;
	}

	/**
	 * Method returns a date when the image was added.
	 * 
	 * @return Date when the image was added
	 */
	public long getDateAdded() {
		return dateAdded;
	}

	/**
	 * Method returns a byte array of the image.
	 * 
	 * @return Byte array of the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Method returns a binary representation of a thumbnail packet.
	 * 
	 * @return binary representation of a thumbnail packet
	 */
	public byte[] getBinaryPacket() {
		return binaryPacket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(300);
		builder.append("ThumbnailPacket { ");
		builder.append("displayName = " + displayName);
		builder.append(", filePath = " + filePath);
		builder.append(", storeId = " + storeId);
		builder.append(", dateAdded = " + dateAdded);
		builder.append(" }");
		return builder.toString();
	}
}
