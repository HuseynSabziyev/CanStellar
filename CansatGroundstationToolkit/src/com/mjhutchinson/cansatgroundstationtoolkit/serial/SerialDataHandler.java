package com.mjhutchinson.cansatgroundstationtoolkit.serial;

import com.google.common.eventbus.Subscribe;

/**
 * Created by Michael Hutchinson on 01/11/2014 at 19:56.
 *
 * An interface describing a class which can handle SerialDataEvents.
 */
public interface SerialDataHandler {

    @Subscribe
    public void serialDataEventHandler(SerialDataEvent event);

}
