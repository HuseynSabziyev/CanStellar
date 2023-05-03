package com.mjhutchinson.cansatgroundstationtoolkit.serial;

/**
 * Created by Michael Hutchinson on 10/03/2015 at 20:47.
 */
public class Packet {

    protected final int PACKET_LENGTH;

    protected byte data[];
    protected int dataFilled = 0;

    public Packet(int packetLength){
        PACKET_LENGTH = packetLength;
        data = new byte[PACKET_LENGTH];
    }

    public boolean add(byte data){
        if(dataFilled == PACKET_LENGTH) return false;
        this.data[dataFilled++] = data;
        return true;
    }

    public boolean isFull(){
        if(dataFilled == PACKET_LENGTH) return true;
        return false;
    }

    public byte[] getData(){
        return data;
    }

}
