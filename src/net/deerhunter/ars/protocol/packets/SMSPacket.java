package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 17.10.12
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class SMSPacket extends BasePacket{
    private String sender;
    private String recipient;
    private String senderPhoneNumber;
    private String recipientPhoneNumber;
    private long time;
    private String text;
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
    public SMSPacket(String sender, String recipient, String senderPhoneNumber, String recipientPhoneNumber,long time, String text){
        this.sender = sender;
        this.recipient = recipient;
        this.senderPhoneNumber = senderPhoneNumber;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.time = time;
        this.text = text;
        generateBinaryPacket();
    }

    /**
     * Method generate a binary representation of a SMS packet
     */
    private void generateBinaryPacket(){
        ByteArrayOutputStream outputArray = new ByteArrayOutputStream(300);
        try{
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
        }
        catch(IOException e){
        }
    }

    /**
     * Method returns a sender name of this SMS
     * @return  sender of SMS
     */
    public String getSender(){
        return sender;
    }

    /**
     * Method returns a recipient name of this SMS
     * @return  recipient of SMS
     */
    public String getRecipient(){
        return recipient;
    }

    /**
     * Method returns a phone number of a sender
     * @return  phone number of a sender
     */
    public String getSenderPhoneNumber(){
        return senderPhoneNumber;
    }

    /**
     * Method returns a phone number of a recipient
     * @return  phone number of a recipient
     */
    public String getRecipientPhoneNumber(){
        return recipientPhoneNumber;
    }


    /**
     * Method returns a time when this SMS was delivered (sent)
     * @return  time when SMS was delivered (sent)
     */
    public long getTime(){
        return time;
    }

    /**
     * Method returns a text of SMS
     * @return  text of SMS
     */
    public String getText(){
        return text;
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
