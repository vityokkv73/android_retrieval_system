package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * This class contains all the information and methods needed to send the
 * locations to the server.
 * 
 * @author DeerHunter
 */
public class LocationPacket extends BasePacket {
	private double latitude;
	private double longitude;
	private double altitude;
	private float accuracy;
	private String provider;
	private long time;
	private byte[] binaryPacket;

	/**
	 * Constructs a new LocationPacket using all necessary parameters.
	 * 
	 * @param latitude Latitude of the location
	 * @param longitude Longitude of the location
	 * @param altitude Altitude of the location
	 * @param accuracy Accuracy of the location
	 * @param provider Provider of the location
	 * @param time Time when this location was given
	 */
	public LocationPacket(double latitude, double longitude, double altitude, float accuracy, String provider, long time) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.accuracy = accuracy;
		this.provider = provider;
		this.time = time;
		generateBinaryPacket();
	}

	/**
	 * Method generate a binary representation of a Location packet.
	 */
	private void generateBinaryPacket() {
		ByteArrayOutputStream outputArray = new ByteArrayOutputStream(300);
		Charset UTF8Charset = Charset.forName("UTF-8");
		try {
			formatWriteStringToArray(outputArray, String.valueOf(latitude), UTF8Charset);
			formatWriteStringToArray(outputArray, String.valueOf(longitude), UTF8Charset);
			formatWriteStringToArray(outputArray, String.valueOf(altitude), UTF8Charset);
			formatWriteStringToArray(outputArray, String.valueOf(accuracy), UTF8Charset);
			formatWriteStringToArray(outputArray, provider, UTF8Charset);

			// write a time to the output stream
			ByteBuffer longArray = ByteBuffer.allocate(8);
			longArray.putLong(time);
			outputArray.write(longArray.array());

			binaryPacket = outputArray.toByteArray();
		} catch (IOException e) {
		}
	}

	/**
	 * Method returns a binary representation of a location packet.
	 * 
	 * @return Binary representation of a location packet
	 */
	public byte[] getBinaryPacket() {
		return binaryPacket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(300);
		builder.append("LocationPacket { ");
		builder.append("latitude = " + latitude);
		builder.append(", longitude = " + longitude);
		builder.append(", altitude = " + altitude);
		builder.append(", accuracy = " + accuracy);
		builder.append(", provider = " + provider);
		builder.append(", time = " + new Date(time));
		builder.append(" }");
		return builder.toString();
	}

	/**
	 * Methods returns a latitude of the location.
	 * @return Latitude of the location
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Methods returns a longitude of the location.
	 * @return Longitude of the location
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Methods returns an altitude of the location.
	 * @return Altitude of the location
	 */
	public double getAltitude() {
		return altitude;
	}

	/**
	 * Methods returns an accuracy of the location.
	 * @return Accuracy of the location
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * Methods returns a provider of the location.
	 * @return Provider of the location
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * Method returns a time when the location was given.
	 * 
	 * @return Time when the location was given
	 */
	public long getTime() {
		return time;
	}
}
