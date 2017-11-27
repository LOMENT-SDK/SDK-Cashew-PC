namespace Cashew.Internal.Encryption
{
    internal interface IKey
    {
        /// <returns>Bytes used to encrypt data in the <see cref="IDataCrypter"/>.</returns>
        byte[] GetKey();
    }
}
