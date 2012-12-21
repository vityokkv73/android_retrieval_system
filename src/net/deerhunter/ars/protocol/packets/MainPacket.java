package net.deerhunter.ars.protocol.packets;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: DeerHunter
 * Date: 17.10.12
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
public class MainPacket {
    private int packetLength;
    private int dataType;
    private byte[] data;
    private byte[] packet;
    private static int HEADER_LENGTH = 8;

    /**
     * Constructs a main packet, that can be sent to the server
     * @param dataType  type of the data in this main packet (constant from DataType class)
     * @param data  data, that will be sent to the server
     */
    public MainPacket(int dataType, byte[] data){
        this.dataType = dataType;
        this.data = data;
        this.packetLength = data.length + HEADER_LENGTH;
        generatePacket();
    }

    /**
     * Method generates a binary representation of the main packet
     */
    private void generatePacket() {
        ByteBuffer buffer = ByteBuffer.allocate(packetLength);
        buffer.putInt(packetLength);
        buffer.putInt(dataType);
        buffer.put(data);
        packet = buffer.array();
    }

    /**
     * Returns length of the main packet
     * @return  packet length
     */
    public int getPacketLength(){
        return packetLength;
    }

    /**
     * Returns type of the data that are held in data array (constant from DataType class)
     * @return  type of the data
     */
    public int getDataType(){
        return dataType;
    }

    /**
     * Returns data that are held in this main packet
     * @return  data that are held in this main packet
     */
    public byte[] getData(){
        return data;
    }

    /**
     * Returns a binary representation of this main packet
     * @return  binary representation of this main packet
     */
    public byte[] getPacket(){
        return packet;
    }
}
