using System;
using Cashew.Services;
using Cashew.Internal.Encryption;

namespace Cashew
{
    /// <summary>
    /// The central entry point for the SDK.
    /// </summary>
    public static class Sdk
    {
        internal static bool IsInitialized { get; private set; }

        internal static IDeviceInformationProvider DeviceInformationProvider { get; private set; }

        internal static IRabbitMQService RabbitMQService { get; private set; }

        internal static string PartnerId { get; set; }

        internal static string ClientId { get; set; }

        /// <summary>
        /// Initializes the SDK with components that are need for operation.
        /// </summary>
        /// <param name="deviceInformationProvider">An implementation to provide necessary device information. <see cref="IDeviceInformationProvider"/> </param>
        /// <param name="rabbitMQService">A platform specific implementation for the RabbitMQ service. <see cref="IRabbitMQService"/></param>
        /// <param name="customDataCrypter">An optional IDataCrypter for custom encryption/decryption. If no IDataCrypter is provided then the default internal encryption is used. <see cref="IDataCrypter"/></param>
        public static void Initialize(IDeviceInformationProvider deviceInformationProvider, IRabbitMQService rabbitMQService, IDataCrypter customDataCrypter = null)
        {
            if (deviceInformationProvider != null)
            {
                DeviceInformationProvider = deviceInformationProvider;
            }
            else
            {
                throw new ArgumentException("The IDeviceInformationProvider can not be null.");
            }

            if (rabbitMQService != null)
            {
                RabbitMQService = rabbitMQService;
            }
            else
            {
                throw new ArgumentException("The IRabbitMQService can not be null.");
            }

            if (customDataCrypter != null)
            {
                Crypto.SetDataCrypter(customDataCrypter);
            }
            else
            {
                Crypto.UseDefaultDataCrypter();
            }

            IsInitialized = true;
        }
    }
}
