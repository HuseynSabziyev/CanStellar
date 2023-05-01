package com.mjhutchinson.cansatgroundstationtoolkit.serial;

import com.google.common.eventbus.Subscribe;

/**
 * Created by Michael Hutchinson on 01/11/2014 at 19:58.
 *
 * An interface describing a class which can handle
 * SerialMessageEvents.
 */
public interface SerialMessageHandler {

    @Subscribe
    public void serialMessageEventHandler(SerialMessageEvent event);

}
