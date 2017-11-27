namespace Cashew.Services
{
    /// <summary>
    /// A device information provider needed by the SDK. Each platform needs to provide an implementation to allow the SDK to function.
    /// <see cref="Sdk.Initialize(IDeviceInformationProvider, IRabbitMQService, IDataCrypter)"/>
    /// </summary>
    public interface IDeviceInformationProvider
    {
#if DEBUG
        string FakeDeviceIDSuffix { get; set; }
#endif
        /// <summary>
        /// A method that provides a unique id for the current device.
        /// </summary>
        string GetUniqueDeviceId();

        /// <summary>
        /// Provides the OS name.
        /// </summary>
        string GetOsName();

        /// <summary>
        /// Indicates the type of device.
        /// </summary>
        DeviceType GetDeviceType();
    }
}
