using Org.BouncyCastle.Crypto.Digests;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Modes;
using Org.BouncyCastle.Crypto.Parameters;
using System;
using System.Diagnostics;
using System.IO;
using System.Text;

namespace Cashew.Internal.Encryption
{
    internal static class Sha256Crypter
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

        private static readonly Sha256Digest Digest = new Sha256Digest();

        /// <summary>
        /// Used for crypt
        /// </summary>
        private static readonly byte[] Key =
        {
            0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 4, 5, 6,
            7, 8, 9, 0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12
        };

        /// <summary>
        /// Generate the SHA256 hash of a string
        /// </summary>
        /// <param name="payload">The string to hash</param>
        /// <returns></returns>
        internal static byte[] GetHash(string payload)
        {
            if (string.IsNullOrEmpty(payload))
            {
                Debug.WriteLine("Can't hash null / empty string");

                return new byte[] { };
            }

            var data = Encoding.UTF8.GetBytes(payload);

            Digest.Reset();
            Digest.BlockUpdate(Key, 0, Key.Length);
            Digest.BlockUpdate(data, 0, data.Length);

            var result = new byte[Digest.GetDigestSize()];

            Digest.DoFinal(result, 0);

            return result;
        }

        /// <summary>
        /// This adds together all the byte[] you'd like to apply, with the internal encryptionKey
        /// </summary>
        /// <param name="payloads"></param>
        /// <returns></returns>
        internal static byte[] GetCombinedHash(params byte[][] payloads)
        {
            var result = new byte[Digest.GetDigestSize()];

            Digest.Reset();

            Digest.BlockUpdate(Key, 0, Key.Length);

            foreach (var payload in payloads)
            {
                Digest.BlockUpdate(payload, 0, payload.Length);
            }

            Digest.DoFinal(result, 0);

            return result;
        }

        /// <summary>
        /// This was marked as obsolete, but seems to be required for decrypting the API key
        /// </summary>
        /// <param name="rawKey"></param>
        /// <returns></returns>
        internal static byte[] GenerateMessageKey(string rawKey)
        {
            var numtimes = 0;
            var len = rawKey.Length;

            for (var i = 0; i < len; i++)
            {
                numtimes += (rawKey.ToCharArray()[i]);
            }

            numtimes = numtimes % len;
            numtimes += len;

            var inb = Encoding.UTF8.GetBytes(rawKey);
            var xor = Encoding.UTF8.GetBytes(rawKey);

            xor = ShiftBytes(xor);

            for (var i = 0; i < numtimes; i++)
            {
                for (var j = 0; j < len; j++)
                {
                    inb[j] = (byte)(inb[j] ^ xor[j]);
                }
                xor = ShiftBytes(xor);
            }

            var digest = new Sha256Digest();

            var tempkey = new byte[digest.GetDigestSize()];

            digest.Reset();
            digest.BlockUpdate(Key, 0, Key.Length);
            digest.BlockUpdate(inb, 0, inb.Length);
            digest.DoFinal(tempkey, 0);

            return tempkey;
        }

        /// <summary>
        /// generate encryptionKey from passphrase, currently just using digest.
        /// </summary>
        /// <param name="rawKey"></param>
        /// <returns></returns>
        internal static byte[] GenerateKey(string rawKey)
        {
            var numtimes = 0;
            var len = rawKey.Length;

            for (var i = 0; i < len; i++)
            {
                numtimes += (rawKey.ToCharArray()[i]);
            }

            numtimes = numtimes % len;
            numtimes += len;

            var inb = Encoding.UTF8.GetBytes(rawKey);
            var xor = Encoding.UTF8.GetBytes(rawKey);

            xor = ShiftBytes(xor);

            var tempkey = new byte[Digest.GetDigestSize()];

            Digest.Reset();
            Digest.BlockUpdate(Key, 0, Key.Length);

            for (var i = 0; i < numtimes; i++)
            {
                Digest.BlockUpdate(xor, 0, xor.Length);
                xor = ShiftBytes(xor);
            }

            Digest.BlockUpdate(inb, 0, inb.Length);
            Digest.DoFinal(tempkey, 0);

            return tempkey;
        }

        /// <summary>
        /// </summary>
        /// <param name="xor"></param>
        /// <returns></returns>
        private static byte[] ShiftBytes(byte[] xor)
        {
            var tmp = new byte[xor.Length];

            Array.Copy(xor, 1, tmp, 0, xor.Length - 1);

            tmp[tmp.Length - 1] = xor[0];

            return tmp;
        }

        /// <summary>
        /// Decrypt SHA256-encrypted data
        /// </summary>
        /// <param name="encryptionKey"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        internal static byte[] Decrypt(byte[] encryptionKey, byte[] data, bool hasPadding = true)
        {
            var param = new ParametersWithIV(new KeyParameter(encryptionKey), InitVector);
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

        /// <summary>
        /// Encrypt data, using encryptionKey
        /// </summary>
        /// <param name="key"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        internal static byte[] Encrypt(byte[] key, byte[] data)
        {
            try
            {
                var byteStream = new MemoryStream();
                var keyParam = new KeyParameter(key, 0, 32);
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
