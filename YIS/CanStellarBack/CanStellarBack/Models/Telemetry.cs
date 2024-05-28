using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CanStellarBack.Models
{
    public class Telemetry
    {
        public string Raw { get; set; }
        public int TeamId { get; set; }

        public DateTime MissionTime { get; set; }

        public int PacketCount { get; set; }

        public char Mode { get; set; }

        public State State { get; set; }

        public double Altitude { get; set; }

        public double AirSpeed { get; set; }

        public char HS_DEPLOYED { get; set; }

        public char PC_DEPLOYED { get; set; }

        public double Temperature { get; set; }

        public double Pressure { get; set; }

        public double Voltage { get; set; }

        public int GPS_Time { get; set; }

        public double GPS_Altitude { get; set; }

        public double GPS_Latitude { get; set; }

        public double GPS_Longitude { get; set; }

        public int GPS_Sats { get; set; }

        public double Tilt_X { get; set; }

        public double Tilt_Y { get; set; }

        public double Rot_Z { get; set; }

        public string CMD_ECHO { get; set; }
       
        public string Note { get; set; }
        
    }
}
