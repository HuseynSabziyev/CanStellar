package com.mjhutchinson.cansatgroundstationtoolkit.serial;

import com.sun.istack.internal.NotNull;

/**
 * An event describing a piece of data received on the
 * SerialEventBus. Split into a kay for type identification
 * and the data itself.
 *
 * Created by Michael Hutchinson on 29/10/2014 at 22:43.
 */
public class SerialDataEvent {

    private final String key;
    private final double data;

    private final double time;

    /**
     * Generates a new SerialDataEvent.
     * @param key the key for the type of data contained.
     * @param data the data to be contained.
     * @param time the time the event occurred.
     */
    public SerialDataEvent(@NotNull String key, @NotNull double data, @NotNull double time){
        this.key = key;
        this.data = data;
        this.time = time;
    }

    /**
     * Gets the SerialDataEvents key
     *
     * @return the event key
     */
    public String getKey(){
        return key;
    }

    /**
     * Gets the SerialEventData data.
     *
     * @return the data from the event.
     */
    public double getData(){
        return data;
    }


    public double getTime() {
        return time;
    }

}
