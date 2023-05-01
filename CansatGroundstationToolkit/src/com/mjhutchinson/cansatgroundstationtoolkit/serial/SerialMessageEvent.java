package com.mjhutchinson.cansatgroundstationtoolkit.serial;

/**
 * An event to describe a message received on the
 * SerialEventBus.
 *
 * Created by Michael Hutchinson on 30/10/2014 at 07:24.
 */
public class SerialMessageEvent {

    private final String message;

    /**
     * Generates a new SerialMessageEvent.
     *
     * @param message the message for the event to contain.
     */
    public SerialMessageEvent(String message) {
        this.message = message;
    }

    /**
     * Gets the event message.
     *
     * @return the event message.
     */
    public String getMessage(){
        return message;
    }
}
