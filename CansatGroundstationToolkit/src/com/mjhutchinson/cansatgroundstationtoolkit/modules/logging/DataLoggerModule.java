package com.mjhutchinson.cansatgroundstationtoolkit.modules.logging;

import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataEvent;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataHandler;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Michael Hutchinson on 10/03/2015 at 23:38.
 */
public class DataLoggerModule extends AbstractModule implements SerialDataHandler{

    Logger logger;
    FileHandler handler;

    public DataLoggerModule(){
        super("Data logger", null);

        logger = Logger.getLogger("com.mjhutchinson.cansatgroundstationtoolkit.modules.logging");
        try {
            handler = new FileHandler("C:\\Users\\Mike\\CansatLogs\\Log.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.setFormatter(new SimpleFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
    }

    @Override
    public void serialDataEventHandler(SerialDataEvent event) {
        logger.log(Level.INFO, event.getKey() + ":" + event.getData() + "@" + event.getTime());
    }
}
