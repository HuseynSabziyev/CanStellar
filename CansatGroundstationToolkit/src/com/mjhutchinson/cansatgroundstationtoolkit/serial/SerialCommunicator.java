package com.mjhutchinson.cansatgroundstationtoolkit.serial;

import com.google.common.eventbus.EventBus;
import jssc.*;

import javax.swing.*;

/**
 * Created by Michael Hutchinson on 29/10/2014 at 22:07.
 *
 * A class to handle serial communications over a specified port.
 * Events are posted using a guava event bus.
 *
 */
public class SerialCommunicator {

    static SerialPort port;

    private static EventBus serialEventBus;

    /**
     * Instantiates a new serial communications module.
     * Adds a shutdown hook in order to close port communications when
     * the program shuts down in order that no ports are left locked open.
     * Instantiates a new guava event bus to handler all the serial data events.
     */
    public SerialCommunicator(){

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownListener()));

        serialEventBus = new EventBus();
    }

    /**
     * Opens a new port with the specified parameters. After every new
     * line character a new SerialDataEvent or SerialMessageEvent is posted
     * to the serialEventBus.
     * @param portName the system String name of the port to be opened
     * @param baudRate the Baud rate at which the port is to be opened at
     * @return returns true if the port has been successfully opened, false otherwise.
     *
     * @see com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataHandler
     * @see com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialMessageEvent
     */
    public boolean openPort(String portName, int baudRate){
        if(port != null){
            if(port.isOpened()){
                try {
                    port.closePort();
                } catch (SerialPortException e) {
                    e.printStackTrace();
                    serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
                    return false;
                }
            }
        }

        port = new SerialPort(portName);
        try {
            port.openPort();
            port.setParams(baudRate, 8, 1, 0);
            port.addEventListener(new SerialPortListener());
            serialEventBus.post(new SerialMessageEvent("Port " + port.getPortName()) + " opened");
        } catch (SerialPortException e) {
            e.printStackTrace();
            serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
            return false;
        }

        return true;
    }

    /**
     * Closes the port currently open. Dose nothing if no port is open.
     */
    public void closePort(){
        if(port != null){
            if(port.isOpened()){
                try {
                    port.removeEventListener();
                    port.closePort();
                    serialEventBus.post(new SerialMessageEvent("Port " + port.getPortName() + " closed"));
                } catch (SerialPortException e) {
                    e.printStackTrace();
                    serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
                }
            }
        }
    }

    /**
     * Sends a message to the serial port.
     * @param message the message to be sent.
     */
    public void writeSerial(String message){
        if(port != null){
            if(port.isOpened()){
                try {
                    port.writeString(message);
                } catch (SerialPortException e) {
                    e.printStackTrace();
                    serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
                }
            }
        }
    }

    /**
     * Change the parameters of the currently selected port. Dose
     * nothing of no port is selected.
     * @param baudRate the new Baud rate at which the port should communicate.
     */
    public void changePortParams(int baudRate){
        if(port != null){
            if(port.isOpened()){
                try {
                    port.setParams(baudRate,  8, 1, 0);
                } catch (SerialPortException e) {
                    e.printStackTrace();
                    serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
                }
            }
        }
    }

    /**
     * Returns a String array of the available ports system names.
     * @return the array of names of the ports
     */
    public String[] getPorts(){
        return SerialPortList.getPortNames();
    }

    /**
     * The listener which waits for serial data on the currently open port and
     * handles the data which comes over the port
     */
    private static class SerialPortListener implements SerialPortEventListener {
        private final byte PACKET_BEGIN = (byte)0xAA;
        private final byte PACKET_END = (byte)0x55;

        private static boolean isReceiving = false;
        private static byte lastByte;
        private static DataPacket packet = new DataPacket();

        //static String accumulatedMessage = "";

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if(serialPortEvent.isRXCHAR()){

                try{

                    byte buffer[] = port.readBytes();

                    if(buffer != null){
                        for(byte b : buffer){

                            if(isReceiving){

                                if(b == PACKET_END){

                                    if(lastByte == PACKET_END){

                                        if(packet.isFull()){

                                            byte checkSum = 0;
                                            for(byte x : packet.getData()){
                                                checkSum ^= x;
                                            }

                                            new DataPacketHandler(packet, serialEventBus);
                                            packet = new DataPacket();

                                        }else{

                                            serialEventBus.post(new SerialMessageEvent("Packet not full, discarding"));
                                            packet = new DataPacket();

                                        }

                                        isReceiving = false;

                                    }

                                }else{

                                    if(lastByte == PACKET_END){

                                        if(!packet.add(lastByte)){
                                            serialEventBus.post(new SerialMessageEvent("Packet full before termination reached, discarding"));
                                            packet = new DataPacket();
                                            isReceiving = false;
                                        }

                                    }

                                    if(!packet.add(b)){
                                        serialEventBus.post(new SerialMessageEvent("Packet full before termination reached, discarding"));
                                        packet = new DataPacket();
                                        isReceiving = false;
                                    }

                                }

                            }else{

                                if(b == PACKET_BEGIN){

                                    if(lastByte == PACKET_BEGIN){

                                        isReceiving = true;

                                        packet = new DataPacket();

                                    }

                                }

                            }

                            lastByte = b;
                        }
                    }

                }catch (SerialPortException e){
                    System.out.println(e.getPortName());
                    System.out.println(e.getExceptionType());
                    System.out.println(e.getMethodName());
                    e.printStackTrace();
                }

            }
        }


//        @Override
//        public void serialEvent(SerialPortEvent serialPortEvent) {
//            if(serialPortEvent.isRXCHAR()){
//
//                try {
//                    byte buffer[] = port.readBytes(serialPortEvent.getEventValue());
//                    for(byte letter: buffer){
//                        accumulatedMessage += (char)letter;
//                    }
//                    if(accumulatedMessage.contains("\n")){
//                        if(accumulatedMessage.contains(":")){
//                            String[] parts = accumulatedMessage.split("\n");
//                            String[] message = parts[0].split(":");
//
//                            serialEventBus.post(new SerialDataEvent(message[0], Double.parseDouble(message[1])));
//
//                            if(parts. length > 1){
//                                accumulatedMessage = parts[1];
//                            }else{
//                                accumulatedMessage = "";
//                            }
//                        }else{
//                            String[] parts = accumulatedMessage.split("\n");
//
//                            serialEventBus.post(new SerialMessageEvent(parts[0]));
//
//                            if(parts. length > 1){
//                                accumulatedMessage = parts[1];
//                            }else{
//                                accumulatedMessage = "";
//                            }
//                        }
//                    }
//                } catch (SerialPortException e) {
//                    e.printStackTrace();
//                    serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
//                }
//
//            }
//        }
    }

    /**
     * Adds an object to the guava event bus's object registry
     * @param registree the object to register
     */
    public void serialRegister(Object registree){
        serialEventBus.register(registree);
    }

    /**
     * Removes an object from the guava event bus's object registry
     * @param unregistree the object to remove
     */
    public void serialUnregister(Object unregistree){
        serialEventBus.unregister(unregistree);
    }

    /**
     * @return returns the guava event bus used to handler serial communications.
     */
    //TODO: remove after testing
    public static EventBus getSerialEventBus() {
        return serialEventBus;
    }

    /**
     * A shutdown listener to close the currently open port if one is open to
     * prevent it being locked open after the program terminates
     */
    static class ShutdownListener implements Runnable {

        @Override
        public void run() {
            if(port != null){
                if(port.isOpened()){
                    try {
                        port.closePort();
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                        serialEventBus.post(new SerialMessageEvent(e.getLocalizedMessage()));
                    }
                }
            }
        }
    }

}
