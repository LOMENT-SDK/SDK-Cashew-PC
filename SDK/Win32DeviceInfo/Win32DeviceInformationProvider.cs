using Cashew.Services;
using System;
using System.Management;

namespace Win32DeviceProvider
{
    /// <summary>
    /// A device information provider for the Win32 platform.
    /// </summary>
    public class Win32DeviceInformationProvider : IDeviceInformationProvider
    {
#if DEBUG
        private string _fakeDeviceIDSuffix;

        public string FakeDeviceIDSuffix
        {
            get { return _fakeDeviceIDSuffix; }
            set { _fakeDeviceIDSuffix = value; _deviceId = null; }
        }


#endif

        private string _deviceId;

        public string GetUniqueDeviceId()
        {
            if (!string.IsNullOrEmpty(_deviceId))
            {
                return _deviceId;
            }

            string cpuId = string.Empty;
            ManagementClass managementClass = new ManagementClass("win32_processor");
            ManagementObjectCollection managementClassCollection = managementClass.GetInstances();

            foreach (ManagementObject mo in managementClassCollection)
            {
                cpuId = mo.Properties["processorID"].Value != null ? mo.Properties["processorID"].Value.ToString() : "";
                if (!string.IsNullOrEmpty(cpuId))
                {
                    break;
                }
            }

#if DEBUG
            _deviceId = string.Format("Win_CPU_{0}{1}", cpuId, FakeDeviceIDSuffix);
#else
            _deviceId = string.Format("Win_CPU_{0}", cpuId);
#endif

            return _deviceId;
        }

        public string GetOsName()
        {
            return Environment.OSVersion.VersionString;
        }

        public DeviceType GetDeviceType()
        {
            return DeviceType.PC;
        }
    }
}
