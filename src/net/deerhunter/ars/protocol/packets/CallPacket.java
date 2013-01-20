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
 * This class contains all the information and methods needed to send the information about the calls
 * to the server.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
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
     * @param caller Caller
     * @param recipient Recipient of the call
     * @param callerPhoneNumber Phone number of a caller
     * @param recipientPhoneNumber Phone number of a recipient
     * @param time Time of the incoming or outgoing call
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
     * Method generate a binary representation of a Call packet.
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
     * Method returns a caller name.
     * @return Caller name
     */
    public String getCaller(){
        return caller;
    }

    /**
     * Method returns a recipient of this call.
     * @return Recipient of the call
     */
    public String getRecipient(){
        return recipient;
    }

    /**
     * Method returns a phone number of a caller.
     * @return Phone number of a caller
     */
    public String getCallerPhoneNumber(){
        return callerPhoneNumber;
    }

    /**
     * Method returns a phone number of a recipient.
     * @return Phone number of a recipient
     */
    public String getRecipientPhoneNumber(){
        return recipientPhoneNumber;
    }


    /**
     * Method returns a time when this call was made.
     * @return Time when the call was made
     */
    public long getTime(){
        return time;
    }

    /**
     * Method returns a binary representation of a CallPacket.
     * @return Binary representation of a CallPacket
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