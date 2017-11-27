namespace Cashew.Internal.Encryption
{
    /// <summary>
    /// Static class for generating a key for use by a <see cref="IDataCrypter"/>.
    /// </summary>
    internal class KeyGeneration
    {
        public static string V0 = "V0";
        public static string V1 = "V1";

        private KeyGeneration() { }

        private static IKey OldKeyGeneration()
        {
            return new PairwiseKey("testuser@loment.net", "test message", false);
        }

        /// <param name="version">Version of the key to generate.</param>
        /// <param name="from">The sender of the message.</param>
        /// <param name="recipient">The recipient of the message.</param>
        /// <returns>A <see cref="IKey"/> to be used by a <see cref="IDataCrypter"/>.</returns>
        internal static IKey GetMessageKey(string version, string from, string recipient)
        {
            if (version == null)
            {
                version = V0;
            }

            if (version.Equals(V1))
            {
                return new PairwiseKey(from, recipient, false);
            } else
            {
                return OldKeyGeneration();
            }
        }

        internal static IKey GetMessageKey(string from, string serverGroupId)
        {
            return new PairwiseKey(from, serverGroupId, true);
        }

    }
}
