using Cashew.Services;

namespace Cashew.Internal.Encryption
{
    /// <summary>
    ///Cryptography package public facing class. Allowing users to override or utilize a <see cref=IDataCrypter/>>.
    /// </summary>
    internal class Crypto
    {
        private static IDataCrypter _dataCrypter = new DefaultDataCrypter();

        internal static IDataCrypter GetDataCrypter()
        {
            if (_dataCrypter == null)
            {
                _dataCrypter = new DefaultDataCrypter();
            }

            return _dataCrypter;
        }

        /// <summary>
        /// Sets a custom <see cref="IDataCrypter"/> to be used for encryption.
        /// </summary>
        /// <param name="inDataCrpyter">The custom DataCrypter to be used.</param>
        internal static void SetDataCrypter(IDataCrypter inDataCrpyter)
        {
            _dataCrypter = inDataCrpyter;
        }

        /// <summary>
        /// Resets the SDK to use the built-in default <see cref="IDataCrypter"/>.
        /// </summary>
        internal static void UseDefaultDataCrypter()
        {
            _dataCrypter = new DefaultDataCrypter();
        }
    }

#if DEBUG
    public class CryptoTestAccess
    {
        public static IDataCrypter GetDataCrypter()
        {
            return Crypto.GetDataCrypter();
        }
    }
#endif
}
