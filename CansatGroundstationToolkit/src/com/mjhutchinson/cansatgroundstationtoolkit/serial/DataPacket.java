package com.mjhutchinson.cansatgroundstationtoolkit.serial;

/**
 * Created by Michael Hutchinson on 10/03/2015 at 21:05.
 */
public class DataPacket extends Packet {

    public static final int DATA_MPU_TIME_LENGTH = 4;
    public static final int DATA_ACCEL_X_LENGTH  = 4;
    public static final int DATA_ACCEL_Y_LENGTH  = 4;
    public static final int DATA_ACCEL_Z_LENGTH  = 4;
    public static final int DATA_GYRO_X_LENGTH   = 4;
    public static final int DATA_GYRO_Y_LENGTH   = 4;
    public static final int DATA_GYRO_Z_LENGTH   = 4;
    public static final int DATA_MAG_X_LENGTH    = 4;
    public static final int DATA_MAG_Y_LENGTH    = 4;
    public static final int DATA_MAG_Z_LENGTH    = 4;
    public static final int DATA_QUAT_A_LENGTH   = 4;
    public static final int DATA_QUAT_B_LENGTH   = 4;
    public static final int DATA_QUAT_C_LENGTH   = 4;
    public static final int DATA_QUAT_D_LENGTH   = 4;
    public static final int DATA_EULER_A_LENGTH  = 4;
    public static final int DATA_EULER_B_LENGTH  = 4;
    public static final int DATA_EULER_G_LENGTH  = 4;
    public static final int DATA_ALT_TIME_LENGTH = 2;
    public static final int DATA_ALT_PRS_LENGTH  = 4;
    public static final int DATA_ALT_TEMP_LENGTH = 4;
    public static final int DATA_GPS_TIME_LENGTH = 2;
    public static final int DATA_GPS_LAT_LENGTH  = 4;
    public static final int DATA_GPS_LON_LENGTH  = 4;
    public static final int DATA_GPS_ALT_LENGTH  = 2;
    public static final int DATA_GPS_BER_LENGTH  = 2;
    public static final int DATA_GPS_SPD_LENGTH  = 2;
    public static final int DATA_ALOG_TIME_LENGTH= 2;
    public static final int DATA_PRESSURE_LENGTH = 2;
    public static final int DATA_TEMP_LENGTH     = 2;
    public static final int DATA_AIR_LENGTH      = 2;
    public static final int DATA_CHECKSUM_LENGTH = 1;

    public static final String DATA_ACCEL_X_KEY  = "AX";
    public static final String DATA_ACCEL_Y_KEY  = "AY";
    public static final String DATA_ACCEL_Z_KEY  = "AZ";
    public static final String DATA_GYRO_X_KEY   = "GX";
    public static final String DATA_GYRO_Y_KEY   = "GY";
    public static final String DATA_GYRO_Z_KEY   = "GZ";
    public static final String DATA_MAG_X_KEY    = "MX";
    public static final String DATA_MAG_Y_KEY    = "MY";
    public static final String DATA_MAG_Z_KEY    = "MZ";
    public static final String DATA_QUAT_A_KEY   = "QA";
    public static final String DATA_QUAT_B_KEY   = "QB";
    public static final String DATA_QUAT_C_KEY   = "QC";
    public static final String DATA_QUAT_D_KEY   = "QD";
    public static final String DATA_EULER_A_KEY  = "EA";
    public static final String DATA_EULER_B_KEY  = "EB";
    public static final String DATA_EULER_G_KEY  = "EG";
    public static final String DATA_ALT_PRS_KEY  = "AP";
    public static final String DATA_ALT_TEM_KEY  = "AT";
    public static final String DATA_GPS_LAT_KEY  = "GN";
    public static final String DATA_GPS_LON_KEY  = "GE";
    public static final String DATA_GPS_ALT_KEY  = "GA";
    public static final String DATA_GPS_BER_KEY  = "GB";
    public static final String DATA_GPS_SPD_KEY  = "GS";
    public static final String DATA_PRESSURE_KEY = "PR";
    public static final String DATA_TEMP_KEY     = "TP";
    public static final String DATA_AIR_KEY      = "AR";

    public DataPacket() {
        super(DATA_MPU_TIME_LENGTH
              + DATA_ACCEL_X_LENGTH
              + DATA_ACCEL_Y_LENGTH
              + DATA_ACCEL_Z_LENGTH
              + DATA_GYRO_X_LENGTH
              + DATA_GYRO_Y_LENGTH
              + DATA_GYRO_Z_LENGTH
              + DATA_MAG_X_LENGTH
              + DATA_MAG_Y_LENGTH
              + DATA_MAG_Z_LENGTH
              + DATA_QUAT_A_LENGTH
              + DATA_QUAT_B_LENGTH
              + DATA_QUAT_C_LENGTH
              + DATA_QUAT_D_LENGTH
              + DATA_EULER_A_LENGTH
              + DATA_EULER_B_LENGTH
              + DATA_EULER_G_LENGTH
              + DATA_ALT_TIME_LENGTH
              + DATA_ALT_TEMP_LENGTH
              + DATA_ALT_PRS_LENGTH
              + DATA_GPS_TIME_LENGTH
              + DATA_GPS_LAT_LENGTH
              + DATA_GPS_LON_LENGTH
              + DATA_GPS_ALT_LENGTH
              + DATA_GPS_BER_LENGTH
              + DATA_GPS_SPD_LENGTH
              + DATA_ALOG_TIME_LENGTH
              + DATA_PRESSURE_LENGTH
              + DATA_TEMP_LENGTH
              + DATA_AIR_LENGTH
              + DATA_CHECKSUM_LENGTH);
    }

}
