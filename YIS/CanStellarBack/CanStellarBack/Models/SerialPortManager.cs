namespace CanStellarBack.Models
{
    using System.IO.Ports;

    public class SerialPortManager
    {
        private static SerialPort _serialPort;
        private static readonly object _lock = new object();

        private SerialPortManager() { }

        public static SerialPort GetSerialPort(string portName, int baudRate)
        {
            lock (_lock)
            {
                if (_serialPort == null)
                {
                    _serialPort = new SerialPort(portName, baudRate);
                }
                return _serialPort;
            }
        }
    }

}
