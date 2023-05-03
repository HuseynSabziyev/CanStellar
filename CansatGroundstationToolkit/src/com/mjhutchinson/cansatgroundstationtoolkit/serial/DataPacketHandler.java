package com.mjhutchinson.cansatgroundstationtoolkit.serial;

import com.google.common.eventbus.EventBus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by Michael Hutchinson on 10/03/2015 at 21:44.
 */
public class DataPacketHandler {

    final float q16 = 65536;
    //final float tempFactor;

    public DataPacketHandler(DataPacket packet, EventBus bus) {

        byte data[] = packet.getData();

        int dataPosition = 0;

        int mpuTime = littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_MPU_TIME_LENGTH)).getInt();

        float acceleration[] = new float[]{littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ACCEL_X_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ACCEL_Y_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ACCEL_Z_LENGTH)).getInt()/q16};

        bus.post(new SerialDataEvent(DataPacket.DATA_ACCEL_X_KEY, acceleration[0], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_ACCEL_Y_KEY, acceleration[1], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_ACCEL_Z_KEY, acceleration[2], mpuTime));

        float gyroscope[] = new float[]{littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GYRO_X_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GYRO_Y_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GYRO_Z_LENGTH)).getInt()/q16};

        bus.post(new SerialDataEvent(DataPacket.DATA_GYRO_X_KEY, gyroscope[0], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_GYRO_Y_KEY, gyroscope[1], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_GYRO_Z_KEY, gyroscope[2], mpuTime));

        float magnetometer[] = new float[]{littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_MAG_X_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_MAG_Y_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_MAG_Z_LENGTH)).getInt()/q16};

        bus.post(new SerialDataEvent(DataPacket.DATA_MAG_X_KEY, magnetometer[0], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_MAG_Y_KEY, magnetometer[1], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_MAG_Z_KEY, magnetometer[2], mpuTime));

        float quaternions[] = new float[]{littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_QUAT_A_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_QUAT_A_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_QUAT_C_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_QUAT_D_LENGTH)).getInt()/q16};

        bus.post(new SerialDataEvent(DataPacket.DATA_QUAT_A_KEY, quaternions[0], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_QUAT_B_KEY, quaternions[1], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_QUAT_C_KEY, quaternions[2], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_QUAT_D_KEY, quaternions[3], mpuTime));

        float eulers[] = new float[]{littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_EULER_A_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_EULER_B_LENGTH)).getInt()/q16,
                littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_EULER_G_LENGTH)).getInt()/q16};

        bus.post(new SerialDataEvent(DataPacket.DATA_EULER_A_KEY, eulers[0], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_EULER_B_KEY, eulers[1], mpuTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_EULER_G_KEY, eulers[2], mpuTime));

        int altTime = mpuTime + littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ALT_TIME_LENGTH)).getShort();

        bus.post(new SerialDataEvent(DataPacket.DATA_ALT_PRS_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ALT_PRS_LENGTH)).getInt(), altTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_ALT_TEM_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ALT_TEMP_LENGTH)).getInt(), altTime));

        int gpsTime = altTime + littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ALT_TIME_LENGTH)).getShort();

        bus.post(new SerialDataEvent(DataPacket.DATA_GPS_LAT_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GPS_LAT_LENGTH)).getInt(), gpsTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_GPS_LON_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GPS_LON_LENGTH)).getInt(), gpsTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_GPS_ALT_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GPS_ALT_LENGTH)).getShort(), gpsTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_GPS_BER_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GPS_BER_LENGTH)).getShort(), gpsTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_GPS_SPD_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_GPS_SPD_LENGTH)).getShort(), gpsTime));

        int alogTime = gpsTime + littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_ALOG_TIME_LENGTH)).getShort();

        bus.post(new SerialDataEvent(DataPacket.DATA_PRESSURE_KEY, littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_PRESSURE_LENGTH)).getShort(), alogTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_TEMP_KEY    , littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_TEMP_LENGTH    )).getShort(), alogTime));
        bus.post(new SerialDataEvent(DataPacket.DATA_AIR_KEY     , littleEndianBuffer(Arrays.copyOfRange(data, dataPosition, dataPosition += DataPacket.DATA_AIR_LENGTH     )).getShort(), alogTime));

        bus.post(new SerialMPUDataEvent(quaternions, acceleration, mpuTime));

        bus.post(new SerialMessageEvent("Packet handled successfully"));
    }

    private ByteBuffer littleEndianBuffer(byte[] array){
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }
}
