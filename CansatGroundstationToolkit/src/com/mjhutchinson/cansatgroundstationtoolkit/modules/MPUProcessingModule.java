package com.mjhutchinson.cansatgroundstationtoolkit.modules;

import com.google.common.eventbus.Subscribe;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataEvent;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialMPUDataEvent;
import com.sun.istack.internal.Nullable;
import org.apache.commons.math3.complex.Quaternion;

import javax.swing.*;

/**
 * Created by Michael Hutchinson on 13/03/2015 at 01:21.
 */
public class MPUProcessingModule extends AbstractModule {

    private float lastData = -1;
    private double position[] = new double[]{0,0,0};
    private double velocity[] = new double[]{0,0,0};

    /**
     * Default constructor for the module.
     *
     * @param title      the title of the module (N.B. must be unique).
     * @param tabbedPane the JTabbedPane in which the module is to appear.
     */
    public MPUProcessingModule(String title, @Nullable JTabbedPane tabbedPane) {
        super(title, tabbedPane);
    }

    @Override
    public void serialDataEventHandler(SerialDataEvent event) {
    }

    @Subscribe
    public void serialMPGEventHandler(SerialMPUDataEvent event){
        if(lastData != -1){
            float quaternions[] = event.getQuaternions();
            float accelerations[] = event.getAcceleration();
            Quaternion quaternion = new Quaternion(quaternions[0], quaternions[1], quaternions[2], quaternions[3]);
            quaternion = quaternion.normalize();
            Quaternion acceleration = new Quaternion(0, accelerations[0], accelerations[1], accelerations[2]);
            acceleration = (quaternion.getInverse().multiply(acceleration.multiply(quaternion))).multiply((event.getTime() - lastData)/1000);
            double[] addVels = new double[]{acceleration.getQ1(), acceleration.getQ2(), acceleration.getQ3()};
            for(int i = 0; i < 3; i++){
                velocity[i] += addVels[i];
            }
            for(int i = 0; i < 3; i++){
                position[i] += velocity[i] * ((event.getTime() - lastData)/1000);
            }

            System.out.println("Position: " + position[0] + ":" + position[1] + ":" + position[2]);
        }

        lastData = event.getTime();
    }
}
