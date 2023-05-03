package com.mjhutchinson.cansatgroundstationtoolkit.serial;

/**
 * Created by Michael Hutchinson on 13/03/2015 at 01:22.
 */
public class SerialMPUDataEvent {

    float quaternions[];
    float acceleration[];

    int time;

    public SerialMPUDataEvent(float[] quaternions, float[] acceleration, int time) {
        this.quaternions = quaternions;
        this.acceleration = acceleration;
        this.time = time;
    }

    public float[] getQuaternions() {
        return quaternions;
    }

    public float[] getAcceleration() {
        return acceleration;
    }

    public int getTime() {
        return time;
    }

}
