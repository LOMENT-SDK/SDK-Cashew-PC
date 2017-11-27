using System.IO;

namespace Cashew.Services
{
    /// <summary>
    /// A interface that defines the necessary methods needed to provide an encryption engine used by the Cashew SDK.
    /// A custom encryption engine can be provided by an implementation of this interface.
    /// <see cref="Sdk.Initialize(IDeviceInformationProvider, IRabbitMQService, IDataCrypter)"/>
    /// </summary>
    public interface IDataCrypter
    {
        /// <param name="data">Data to be encrypted.</param>
        /// <param name="sender">User sending the message being encrypted.</param>
        /// <param name="recipient">User receiving the message being encrypted.</param>
        /// <returns>The encrypted data.</returns>
        byte[] EncryptData(byte[] data, string sender, string recipient);

        /// <param name="data">Data to be decrypted.</param>
        /// <param name="sender">User sending the message to be decrypted.</param>
        /// <param name="recipient">User receiving the message being decrypted.</param>
        /// <returns>The decrypted data.</returns>
        byte[] DecryptData(byte[] data, string sender, string recipient);

        /// <param name="data">Data to be encrypted.</param>
        /// <param name="sender">User sending the message to be decrypted.</param>
        /// <param name="groupId">Unique ID of the group the message belongs to.</param>
        /// <returns>The encrypted data.</returns>
        byte[] EncryptGroupData(byte[] data, string sender, string groupId);

        /// <param name="data">Data to be decrypted.</param>
        /// <param name="sender">User sending the message to be decrypted.</param>
        /// <param name="groupId">Unique ID of the group the message belongs to.</param>
        /// <returns>The decrypted data.</returns>
        byte[] DecryptGroupData(byte[] data, string sender, string groupId);

        /// <param name="inStream">Data to be encrypted.</param>
        /// <param name="outStream">The encrypted data.</param>
        /// <param name="sender">User sending the message being encrypted.</param>
        /// <param name="recipient">User receiving the message being encrypted.</param>
        void EncryptStream(Stream inStream, Stream outStream, string sender, string recipient);

        /// <param name="inStream">Data to be decrypted.</param>
        /// <param name="outStream">The decrypted data.</param>
        /// <param name="sender">User sending the message being decrypted.</param>
        /// <param name="recipient">User receiving the message being decrypted.</param>
        /// <param name="inSize">Size of the encrypted 'in' stream, in bytes.</param>
        /// <param name="outSize">The original file size, in bytes. -1 if unknown.</param>
        void DecryptStream(Stream inStream, Stream outStream, string sender, string recipient, long inSize, long outSize);

        /// <param name="inStream">Data to be encrypted.</param>
        /// <param name="outStream">The encrypted data.</param>
        /// <param name="sender">User sending the message being encrypted.</param>
        /// <param name="groupId">Unique ID of the group the message belongs to.</param>
        void EncryptGroupStream(Stream inStream, Stream outStream, string sender, string groupId);

        /// <param name="inStream">Data to be decrypted.</param>
        /// <param name="outStream">The decrypted data.</param>
        /// <param name="sender">User sending the message being decrypted.</param>
        /// <param name="groupId">Unique ID of the group the message belongs to.</param>
        /// <param name="inSize">Size of the encrypted 'in' stream, in bytes.</param>
        /// <param name="outSize">The original file size, in bytes. -1 if unknown.</param>
        void DecryptGroupStream(Stream inStream, Stream outStream, string sender, string groupId, long inSize, long outSize);
    }
}
