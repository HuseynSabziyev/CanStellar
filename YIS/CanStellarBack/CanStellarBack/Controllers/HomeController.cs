using CanStellarBack.Hubs;
using CanStellarBack.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.IO.Ports;
using System.Threading.Tasks;

namespace CanStellarBack.Controllers
{
    public class HomeController : Controller
    {
        private readonly IHubContext<TelemetryHub> _hubContext;

        public HomeController(IHubContext<TelemetryHub> hubContext)
        {

            _hubContext = hubContext;

        }

        SerialPort serialPort = new SerialPort("COM4", 9600);

        public IActionResult Index()
        {
            //serialPort.Open();
            return View();

        }

        [HttpPost]
        public void TelemetryFunc(int key)
        {

            if (key==1)
            {

                serialPort.Write($"CMD,2094,CX,ON");

                StartTelemetryUpdates(serialPort);

            }
            else if(key==0)
            {

                serialPort.Write($"CMD,2094,CX,OFF");
                
            }

        }

        [HttpPost]
        public void SetTime()
        {

            serialPort.Write($"CMD,2094,ST,{DateTime.Now.ToString()}");
            
        }

        [HttpPost]
        public void SimulationControl(int key) 
        {

            if (key == 2)
            {

                serialPort.Write($"CMD,2094,SIM,ENABLE");

            }
            else if (key == 1)
            {

                serialPort.Write($"CMD,2094,SIM,ACTIVATE");

            }
            else if(key==0)
            {

                serialPort.Write($"CMD,2094,SIM,DISABLE");

            }

        }

        [HttpPost]
        public void SimulatePressure(int Pressure)
        {

            serialPort.Write($"CMD,2094,SIMP,{Pressure}");

        }

        [HttpPost]
        public void CalibrateAltitude()
        {

            serialPort.Write($"CMD,2094,CAL");

        }

        [HttpPost]
        public void ControlAudio(int key)
        {

            if (key == 1)
            {
                serialPort.Write($"CMD,2094,BCN,ON");

            }
            else if (key == 0)
            {
                serialPort.Write($"CMD,2094,BCN,OFF");

            }

        }


        private async Task StartTelemetryUpdates(SerialPort serialPort)
        {

            while (true)
            {

                string receivedData = serialPort.ReadLine();

                ProcessAndWriteToCsv(receivedData);

                Telemetry telemetry = ProcessReceivedData(receivedData);

                await _hubContext.Clients.All.SendAsync("ReceiveTelemetryData", telemetry);

            }
        }

        public void StopProgram()
        {

            serialPort.Close();

            Environment.Exit(0);

        }

        private Telemetry ProcessReceivedData(string data)
        {
            Telemetry telemetry = new Telemetry();
            telemetry.Raw = data;
            //data = data.Replace("<", "").Replace(">", "");
            string[] entries = data.Split(';');
            if (entries.Length > 0)
            {
                string[] values = entries[0].Split(',');
                telemetry.TeamId = int.Parse(values[0].Trim());
                telemetry.MissionTime = DateTime.Parse(values[1].Trim());
                telemetry.PacketCount = int.Parse(values[2].Trim(), CultureInfo.InvariantCulture);
                telemetry.State = (State)int.Parse(values[3]);
                telemetry.Altitude = double.Parse(values[4].Trim(), CultureInfo.InvariantCulture);
                telemetry.AirSpeed = double.Parse(values[5].Trim(), CultureInfo.InvariantCulture);
                telemetry.HS_DEPLOYED = values[6].Trim()[0];
                telemetry.PC_DEPLOYED = values[7].Trim()[0];
                telemetry.Temperature = double.Parse(values[8].Trim(), CultureInfo.InvariantCulture);
                telemetry.Pressure= double.Parse(values[9].Trim(), CultureInfo.InvariantCulture);
                telemetry.Voltage= double.Parse(values[10].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Time = int.Parse(values[11].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Altitude= double.Parse(values[12].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Latitude = double.Parse(values[13].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Longitude= double.Parse(values[14].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Sats = int.Parse(values[15].Trim(), CultureInfo.InvariantCulture);
                telemetry.Tilt_X= double.Parse(values[16].Trim(), CultureInfo.InvariantCulture);
                telemetry.Tilt_Y= double.Parse(values[17].Trim(), CultureInfo.InvariantCulture);
                telemetry.Rot_Z = double.Parse(values[18].Trim(), CultureInfo.InvariantCulture);
                telemetry.CMD_ECHO = values[19].Trim();
                telemetry.Note = values[20].Trim();
            }
            return telemetry;
        }

        private void ProcessAndWriteToCsv(string receivedData)
        {
            string dataFolderPath = Path.Combine(Directory.GetCurrentDirectory(), "data");
            string filePath = Path.Combine(dataFolderPath, "Flight_2094.csv");
            Directory.CreateDirectory(dataFolderPath);

            using (StreamWriter writer = new StreamWriter(filePath, true))
            {
                // Split the received data into separate lines based on semicolons
                string[] entries = receivedData.Split(';');
                foreach (string entry in entries)
                {
                    writer.WriteLine(entry);  // Write each entry as a new line in the CSV
                }
            }
        }
    }
}