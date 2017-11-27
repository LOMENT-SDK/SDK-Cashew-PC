using System;

namespace Cashew.Services
{
    /// <summary>
    /// Represents an exception that can occur during the encryption process.
    /// </summary>
    public class CryptoException : Exception
    {
        public CryptoException(string detailMessage) : base(detailMessage) {}
    }
}
