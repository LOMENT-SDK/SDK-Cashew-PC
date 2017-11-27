using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Modes;
using Org.BouncyCastle.Crypto.Parameters;
using System;
using System.Diagnostics;
using System.IO;

namespace Cashew.Internal.Encryption
{
    internal class Crypter
    {
        /// <summary>
        /// Used for decrypting
        /// </summary>
        private static readonly byte[] InitVector = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 16 };

        /// <summary>
        /// The engine used to decrypt SHA256 encoded data
        /// </summary>
        private static readonly CbcBlockCipher DecryptCipher = new CbcBlockCipher(new AesEngine());

        /// <summary>
        /// The engine used to encrypt SHA256 encoded data
        /// </summary>
        private static readonly CbcBlockCipher EncryptCipher = new CbcBlockCipher(new AesEngine());

        /// <param name="key">The key to be used to decrypt the data.</param>
        /// <param name="data">The encrypted data.</param>
        /// <param name="hasPadding">Whether the encrypted data includes padding.</param>
        /// <returns>The decrypted data.</returns>
        internal static byte[] DecryptNew(IKey key, byte[] data, bool hasPadding = true)
        {
            var param = new ParametersWithIV(new KeyParameter(key.GetKey()), InitVector);
            var clear = new byte[data.Length];

            DecryptCipher.Init(false, param);
            var blocksize = DecryptCipher.GetBlockSize();

            for (var i = 0; i < data.Length; i += blocksize)
            {
                DecryptCipher.ProcessBlock(data, i, clear, i);
            }

            DecryptCipher.Reset();

            var inStream = new MemoryStream(clear);

            if (hasPadding)
            {
                var paddingSize = 0;
                for (var i = clear.Length - 1; i >= clear.Length - blocksize; i--)
                {
                    if (clear[i] == 0)
                        paddingSize++;
                    else
                        break;
                }

                var outb = new byte[clear.Length - paddingSize];
                inStream.Read(outb, 0, outb.Length);
                return outb;
            }

            return clear;
        }

        /// <param name="key">The key to be used to encrypt the data.</param>
        /// <param name="data">The data to be encrypted.</param>
        /// <returns>The encrypted data.</returns>
        internal static byte[] EncryptNew(IKey key, byte[] data)
        {
            try
            {
                var byteStream = new MemoryStream();
                var keyParam = new KeyParameter(key.GetKey(), 0, 32);
                var param = new ParametersWithIV(keyParam, InitVector);

                EncryptCipher.Init(true, param);

                var blocksize = EncryptCipher.GetBlockSize();

                byteStream.Write(data, 0, data.Length);

                var blocks = data.Length / blocksize;

                if ((data.Length % blocksize) > 0)
                {
                    blocks++;
                }

                var inb = new byte[blocksize * blocks];

                Array.Copy(byteStream.ToArray(), 0, inb, 0, (int)(byteStream.Length));

                var outb = new byte[inb.Length];

                for (var i = 0; i < inb.Length; i += blocksize)
                {
                    EncryptCipher.ProcessBlock(inb, i, outb, i);
                }

                EncryptCipher.Reset();

                return outb;
            }
            catch (Exception e)
            {
                Debug.WriteLine(e.StackTrace);

                return new byte[] { 0xff };
            }
        }
    }
}
