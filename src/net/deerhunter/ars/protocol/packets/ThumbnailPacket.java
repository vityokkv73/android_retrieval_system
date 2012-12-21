package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import android.util.Log;

public class ThumbnailPacket extends BasePacket {
	private String displayName;
    private String filePath;
    private int storeId;
    private long dateAdded;
    private byte[] image;
    
    private byte[] binaryPacket;
    
    /**
     * Constructs a new SMSPacket using all necessary parameters.
     * @param sender  sender of this SMS
     * @param recipient  recipient of this SMS
     * @param senderPhoneNumber  phone number of a sender
     * @param recipientPhoneNumber  phone number of a recipient
     * @param time  SMS sending or delivery time
     * @param text  SMS text
     */
    public ThumbnailPacket(String displayName, String filePath, int storeId, long dateAdded, byte[] image){
        this.displayName = displayName;
        this.filePath = filePath;
        this.storeId = storeId;
        this.dateAdded = dateAdded;
        this.image = image;
        generateBinaryPacket();
    }

    /**
     * Method generate a binary representation of a SMS packet
     */
    private void generateBinaryPacket(){
        ByteArrayOutputStream outputArray = new ByteArrayOutputStream(100000);
        try{
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
        }
        catch(IOException e){
        }
    }

    /**
     * Method returns a sender name of this SMS
     * @return  sender of SMS
     */
    public String getDisplayName(){
        return displayName;
    }

    /**
     * Method returns a recipient name of this SMS
     * @return  recipient of SMS
     */
    public String getFilePath(){
        return filePath;
    }

    /**
     * Method returns a phone number of a sender
     * @return  phone number of a sender
     */
    public int getStoreId(){
        return storeId;
    }
    
    public long getDateAdded(){
    	return dateAdded;
    }

    /**
     * Method returns a phone number of a recipient
     * @return  phone number of a recipient
     */
    public byte[] getImage(){
        return image;
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
        builder.append("ThumbnailPacket { ");
        builder.append("displayName = " + displayName);
        builder.append(", filePath = " + filePath);
        builder.append(", storeId = " + storeId);
        builder.append(", dateAdded = " + dateAdded);
        builder.append(" }");
        return builder.toString();
    }
}
