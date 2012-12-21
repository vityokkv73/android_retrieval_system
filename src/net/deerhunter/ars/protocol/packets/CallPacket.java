package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 21.10.12
 * Time: 0:02
 * To change this template use File | Settings | File Templates.
 */
public class CallPacket extends BasePacket{
    private String caller;
    private String recipient;
    private String callerPhoneNumber;
    private String recipientPhoneNumber;
    private long time;
    private byte[] binaryPacket;

    /**
     * Constructs a new CallPacket using all necessary parameters.
     * @param caller  caller
     * @param recipient  recipient of the call
     * @param callerPhoneNumber  phone number of a caller
     * @param recipientPhoneNumber  phone number of a recipient
     * @param time  call's incoming or outgoing time
     */
    public CallPacket(String caller, String recipient, String callerPhoneNumber, String recipientPhoneNumber,long time){
        this.caller = caller;
        this.recipient = recipient;
        this.callerPhoneNumber = callerPhoneNumber;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.time = time;
        generateBinaryPacket();
    }

    /**
     * Method generate a binary representation of a SMS packet
     */
    private void generateBinaryPacket(){
        ByteArrayOutputStream outputArray = new ByteArrayOutputStream(300);
        try{
            Charset UTF8Charset = Charset.forName("UTF-8");

            formatWriteStringToArray(outputArray, caller, UTF8Charset);
            formatWriteStringToArray(outputArray, recipient, UTF8Charset);
            formatWriteStringToArray(outputArray, callerPhoneNumber, UTF8Charset);
            formatWriteStringToArray(outputArray, recipientPhoneNumber, UTF8Charset);

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
     * Method returns a caller name
     * @return  caller
     */
    public String getCaller(){
        return caller;
    }

    /**
     * Method returns a recipient of this call
     * @return  recipient of the call
     */
    public String getRecipient(){
        return recipient;
    }

    /**
     * Method returns a phone number of a caller
     * @return  phone number of a caller
     */
    public String getCallerPhoneNumber(){
        return callerPhoneNumber;
    }

    /**
     * Method returns a phone number of a recipient
     * @return  phone number of a recipient
     */
    public String getRecipientPhoneNumber(){
        return recipientPhoneNumber;
    }


    /**
     * Method returns a time when this call was made
     * @return  time when the call was made
     */
    public long getTime(){
        return time;
    }

    /**
     * Method returns a binary representation of a CallPacket
     * @return  binary representation of a CallPacket
     */
    public byte[] getBinaryPacket(){
        return binaryPacket;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(300);
        builder.append("CallPacket { ");
        builder.append("caller = " + caller);
        builder.append(", recipient = " + recipient);
        builder.append(", callerPhoneNumber = " + callerPhoneNumber);
        builder.append(", recipientPhoneNumber = " + recipientPhoneNumber);
        builder.append(", time = " + new Date(time));
        builder.append(" }");
        return builder.toString();
    }
}