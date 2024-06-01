using CanStellarBack.Hubs;
using CanStellarBack.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.IO.Ports;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace CanStellarBack.Controllers
{
    public class HomeController : Controller
    {
        private readonly IHubContext<TelemetryHub> _hubContext;
        private static CancellationTokenSource _cancellationTokenSource;
        private static readonly object _lock = new object();

        public HomeController(IHubContext<TelemetryHub> hubContext)
        {
            _hubContext = hubContext;
        }

        public IActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public async Task TelemetryFunc(int key = 1)
        {
            try
            {
                SerialPort _serialPort = SerialPortManager.GetSerialPort("COM6", 9600);
                lock (_lock)
                {
                    if (!_serialPort.IsOpen)
                    {
                        _serialPort.Open();
                    }
                }

                if (key == 1)
                {
                    _serialPort.WriteLine($"CMD,2094,CX,ON");
                    _cancellationTokenSource = new CancellationTokenSource();
                    _ = StartTelemetryUpdates(_serialPort, _cancellationTokenSource.Token);
                }
                else if (key == 0)
                {
                    _serialPort.WriteLine($"CMD,2094,CX,OFF");
                    _cancellationTokenSource.Cancel();
                }
            }
            catch (Exception ex)
            {
                // Handle the exception here
            }
        }

        [HttpPost]
        public async Task SetTime()
        {
            await ExecuteSerialCommand(async _serialPort =>
            {
                _serialPort.WriteLine($"CMD,2094,ST,{DateTime.Now.ToString()}");
            });
        }

        [HttpPost]
        public async Task SimulationControl(int key)
        {
            await ExecuteSerialCommand(async _serialPort =>
            {
                await Task.Delay(1000);
                if (key == 2)
                {
                    _serialPort.WriteLine($"CMD,2094,SIM,ENABLE");
                }
                else if (key == 1)
                {
                    _serialPort.WriteLine($"CMD,2094,SIM,ACTIVATE");
                }
                else if (key == 0)
                {
                    _serialPort.WriteLine($"CMD,2094,SIM,DISABLE");
                }
            });
        }

        [HttpPost]
        public async Task SimulatePressure(int Pressure)
        {
            await ExecuteSerialCommand(async _serialPort =>
            {
                await Task.Delay(1000);
                _serialPort.WriteLine($"CMD,2094,SIMP,{Pressure}");
            });
        }

        [HttpPost]
        public async Task CalibrateAltitude()
        {
            await ExecuteSerialCommand(async _serialPort =>
            {
                await Task.Delay(1000);
                _serialPort.WriteLine($"CMD,2094,CAL");
            });
        }

        [HttpPost]
        public async Task ControlAudio(int key)
        {
            await ExecuteSerialCommand(async _serialPort =>
            {
                await Task.Delay(1000);
                if (key == 1)
                {
                    _serialPort.WriteLine($"CMD,2094,BCN,ON");
                }
                else if (key == 0)
                {
                    _serialPort.WriteLine($"CMD,2094,BCN,OFF");
                }
            });
        }

        private async Task ExecuteSerialCommand(Func<SerialPort, Task> command)
        {
            try
            {
                SerialPort _serialPort = SerialPortManager.GetSerialPort("COM6", 9600);
                lock (_lock)
                {
                    if (!_serialPort.IsOpen)
                    {
                        _serialPort.Open();
                    }
                }

                await command(_serialPort);
            }
            catch (Exception ex)
            {
                // Handle the exception here
            }
        }

        private async Task StartTelemetryUpdates(SerialPort serialPort, CancellationToken cancellationToken)
        {
            try
            {
                StringBuilder receivedDataBuffer = new StringBuilder();
                byte[] buffer = new byte[1024];
                while (!cancellationToken.IsCancellationRequested)
                {
                    int bytesRead = await serialPort.BaseStream.ReadAsync(buffer, 0, buffer.Length, cancellationToken);
                    if (bytesRead > 0)
                    {
                        string receivedChunk = Encoding.ASCII.GetString(buffer, 0, bytesRead);
                        receivedDataBuffer.Append(receivedChunk);

                        int newLineIndex;
                        while ((newLineIndex = receivedDataBuffer.ToString().IndexOf('\n')) >= 0)
                        {
                            string completeLine = receivedDataBuffer.ToString(0, newLineIndex).Trim();
                            receivedDataBuffer.Remove(0, newLineIndex + 1);

                            ProcessAndWriteToCsv(completeLine);
                            Telemetry telemetry = ProcessReceivedData(completeLine);
                            await _hubContext.Clients.All.SendAsync("ReceiveTelemetryData", telemetry);
                        }
                    }
                }
            }
            catch (OperationCanceledException)
            {
                // Handle the cancellation gracefully
            }
            finally
            {
                lock (_lock)
                {
                    serialPort.Close();
                }
            }
        }

        private Telemetry ProcessReceivedData(string data)
        {
            Telemetry telemetry = new Telemetry();
            telemetry.Raw = data;
            data = data.Replace("<", "").Replace(">", "");
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
                telemetry.Pressure = double.Parse(values[9].Trim(), CultureInfo.InvariantCulture);
                telemetry.Voltage = double.Parse(values[10].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Time = int.Parse(values[11].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Altitude = double.Parse(values[12].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Latitude = double.Parse(values[13].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Longitude = double.Parse(values[14].Trim(), CultureInfo.InvariantCulture);
                telemetry.GPS_Sats = int.Parse(values[15].Trim(), CultureInfo.InvariantCulture);
                telemetry.Tilt_X = double.Parse(values[16].Trim(), CultureInfo.InvariantCulture);
                telemetry.Tilt_Y = double.Parse(values[17].Trim(), CultureInfo.InvariantCulture);
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
                string[] entries = receivedData.Split(';');
                foreach (string entry in entries)
                {
                    writer.WriteLine(entry);
                }
            }
        }
    }
}




//using CanStellarBack.Hubs;
//using CanStellarBack.Models;
//using Microsoft.AspNetCore.Mvc;
//using Microsoft.AspNetCore.SignalR;
//using System;
//using System.Collections.Generic;
//using System.Globalization;
//using System.IO;
//using System.IO.Ports;
//using System.Threading.Tasks;

//namespace CanStellarBack.Controllers
//{
//    public class HomeController : Controller
//    {
//        private readonly IHubContext<TelemetryHub> _hubContext;


//        public HomeController(IHubContext<TelemetryHub> hubContext)
//        {

//            _hubContext = hubContext;

//        }



//        public IActionResult Index()
//        {

//            //try
//            //{
//            //    _serialPort = new SerialPort("COM6", 9600);
//            //    if (!_serialPort.IsOpen)
//            //    {
//            //        _serialPort.Open();
//            //    }
//            //}
//            //catch (Exception ex)
//            //{
//            //    // Handle the exception here
//            //}

//            return View();
//        }

//        [HttpPost]
//        public void TelemetryFunc(int key = 1)
//        {
//            try
//            {
//                SerialPort _serialPort = new SerialPort("COM6", 9600);


//                _serialPort.Open();


//                if (key == 1)
//                {
//                    _serialPort.WriteLine($"CMD,2094,CX,ON");
//                    StartTelemetryUpdates(_serialPort);
//                    _serialPort.Close();
//                }
//                else if (key == 0)
//                {
//                    _serialPort.WriteLine($"CMD,2094,CX,OFF");
//                    _serialPort.Close();
//                }
//            }
//            catch (Exception ex)
//            {
//                // Handle the exception here
//            }
//        }

//        [HttpPost]
//        public void SetTime()
//        {
//            try
//            {
//                SerialPort _serialPort = new SerialPort("COM6", 9600);


//                _serialPort.Open();


//                _serialPort.WriteLine($"CMD,2094,ST,{DateTime.Now.ToString()}");
//                _serialPort.Close();
//            }
//            catch (Exception ex)
//            {
//                // Handle the exception here
//            }
//        }

//        [HttpPost]
//        public void SimulationControl(int key)
//        {
//            try
//            {
//                SerialPort _serialPort = new SerialPort("COM6", 9600);

//                _serialPort.Open();
//                System.Threading.Thread.Sleep(1000);

//                if (key == 2)
//                {
//                    _serialPort.WriteLine($"CMD,2094,SIM,ENABLE");
//                    _serialPort.Close();
//                }
//                else if (key == 1)
//                {
//                    _serialPort.WriteLine($"CMD,2094,SIM,ACTIVATE");
//                    _serialPort.Close();
//                }
//                else if (key == 0)
//                {
//                    _serialPort.WriteLine($"CMD,2094,SIM,DISABLE");
//                    _serialPort.Close();
//                }
//            }
//            catch (Exception ex)
//            {
//                // Handle the exception here
//            }
//        }

//        [HttpPost]
//        public void SimulatePressure(int Pressure)
//        {
//            try
//            {
//                SerialPort _serialPort = new SerialPort("COM6", 9600);
//                //if (!_serialPort.IsOpen)
//                //{
//                //    _serialPort.Open();
//                //}

//                _serialPort.Open();

//                System.Threading.Thread.Sleep(1000);
//                _serialPort.WriteLine($"CMD,2094,SIMP,{Pressure}");
//                _serialPort.Close();
//            }
//            catch (Exception ex)
//            {
//                // Handle the exception here
//            }
//        }

//        [HttpPost]
//        public void CalibrateAltitude()
//        {
//            try
//            {
//                SerialPort _serialPort = new SerialPort("COM6", 9600);

//                _serialPort.Open();


//                System.Threading.Thread.Sleep(1000);
//                _serialPort.WriteLine($"CMD,2094,CAL");
//                _serialPort.Close();
//            }
//            catch (Exception ex)
//            {
//                // Handle the exception here
//            }
//        }

//        [HttpPost]
//        public void ControlAudio(int key)
//        {
//            try
//            {
//                SerialPort _serialPort = new SerialPort("COM6", 9600);

//                _serialPort.Open();


//                System.Threading.Thread.Sleep(1000);
//                if (key == 1)
//                {
//                    _serialPort.WriteLine($"CMD,2094,BCN,ON");
//                    _serialPort.Close();
//                }
//                else if (key == 0)
//                {
//                    _serialPort.WriteLine($"CMD,2094,BCN,OFF");
//                    _serialPort.Close();
//                }
//            }
//            catch (Exception ex)
//            {
//                // Handle the exception here
//            }
//        }


//        private async Task StartTelemetryUpdates(SerialPort serialPort)
//        {

//            while (true)
//            {

//                string receivedData = serialPort.ReadLine();

//                ProcessAndWriteToCsv(receivedData);

//                Telemetry telemetry = ProcessReceivedData(receivedData);

//                await _hubContext.Clients.All.SendAsync("ReceiveTelemetryData", telemetry);

//            }
//        }

//        public void StopProgram()
//        {


//            Environment.Exit(0);

//        }

//        private Telemetry ProcessReceivedData(string data)
//        {
//            Telemetry telemetry = new Telemetry();
//            telemetry.Raw = data;
//            data = data.Replace("<", "").Replace(">", "");
//            string[] entries = data.Split(';');
//            if (entries.Length > 0)
//            {
//                string[] values = entries[0].Split(',');
//                telemetry.TeamId = int.Parse(values[0].Trim());
//                telemetry.MissionTime = DateTime.Parse(values[1].Trim());
//                telemetry.PacketCount = int.Parse(values[2].Trim(), CultureInfo.InvariantCulture);
//                telemetry.State = (State)int.Parse(values[3]);
//                telemetry.Altitude = double.Parse(values[4].Trim(), CultureInfo.InvariantCulture);
//                telemetry.AirSpeed = double.Parse(values[5].Trim(), CultureInfo.InvariantCulture);
//                telemetry.HS_DEPLOYED = values[6].Trim()[0];
//                telemetry.PC_DEPLOYED = values[7].Trim()[0];
//                telemetry.Temperature = double.Parse(values[8].Trim(), CultureInfo.InvariantCulture);
//                telemetry.Pressure = double.Parse(values[9].Trim(), CultureInfo.InvariantCulture);
//                telemetry.Voltage = double.Parse(values[10].Trim(), CultureInfo.InvariantCulture);
//                telemetry.GPS_Time = int.Parse(values[11].Trim(), CultureInfo.InvariantCulture);
//                telemetry.GPS_Altitude = double.Parse(values[12].Trim(), CultureInfo.InvariantCulture);
//                telemetry.GPS_Latitude = double.Parse(values[13].Trim(), CultureInfo.InvariantCulture);
//                telemetry.GPS_Longitude = double.Parse(values[14].Trim(), CultureInfo.InvariantCulture);
//                telemetry.GPS_Sats = int.Parse(values[15].Trim(), CultureInfo.InvariantCulture);
//                telemetry.Tilt_X = double.Parse(values[16].Trim(), CultureInfo.InvariantCulture);
//                telemetry.Tilt_Y = double.Parse(values[17].Trim(), CultureInfo.InvariantCulture);
//                telemetry.Rot_Z = double.Parse(values[18].Trim(), CultureInfo.InvariantCulture);
//                telemetry.CMD_ECHO = values[19].Trim();
//                telemetry.Note = values[20].Trim();
//            }
//            return telemetry;
//        }

//        private void ProcessAndWriteToCsv(string receivedData)
//        {
//            string dataFolderPath = Path.Combine(Directory.GetCurrentDirectory(), "data");
//            string filePath = Path.Combine(dataFolderPath, "Flight_2094.csv");
//            Directory.CreateDirectory(dataFolderPath);

//            using (StreamWriter writer = new StreamWriter(filePath, true))
//            {
//                // Split the received data into separate lines based on semicolons
//                string[] entries = receivedData.Split(';');
//                foreach (string entry in entries)
//                {
//                    writer.WriteLine(entry);  // Write each entry as a new line in the CSV
//                }
//            }
//        }
//    }
//}
