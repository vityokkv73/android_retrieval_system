package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

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
	     * @param sender  sender of this SMS
	     * @param recipient  recipient of this SMS
	     * @param senderPhoneNumber  phone number of a sender
	     * @param recipientPhoneNumber  phone number of a recipient
	     * @param time  SMS sending or delivery time
	     * @param text  SMS text
	     */
	    public LocationPacket(double latitude, double longitude, double altitude, float accuracy, String provider, long time){
	        this.latitude = latitude;
	        this.longitude = longitude;
	        this.altitude = altitude;
	        this.accuracy = accuracy;
	        this.provider = provider;
	        this.time = time;
	        generateBinaryPacket();
	    }

	    /**
	     * Method generate a binary representation of a Location packet
	     */
	    private void generateBinaryPacket(){
	        ByteArrayOutputStream outputArray = new ByteArrayOutputStream(300);
	        Charset UTF8Charset = Charset.forName("UTF-8");
	        try{   
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
	        }
	        catch(IOException e){
	        }
	    }  

	    /**
	     * Method returns a binary representation of a SMS packet
	     * @return  binary representation of a SMS packet
	     */
	    public byte[] getBinaryPacket(){
	        return binaryPacket;
	    }

	    @Override
	    public String toString(){
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

		public double getLatitude() {
			return latitude;
		}		

		public double getLongitude() {
			return longitude;
		}

		public double getAltitude() {
			return altitude;
		}

		public float getAccuracy() {
			return accuracy;
		}

		public String getProvider() {
			return provider;
		}
		
		 /**
	     * Method returns a time when this location was got
	     * @return  time when location was got
	     */
	    public long getTime(){
	        return time;
	    }	   
}
