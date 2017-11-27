using System;
using System.Text;
using Org.BouncyCastle.Crypto.Digests;


namespace Cashew.Internal.Encryption
{
    internal class MessageKey : IKey
    {
        private byte[] _key = { 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12 };

        /// <param name="keyphrase">The string used to generate an encryption key.</param>
        internal MessageKey(string keyphrase)
        {
            GenerateKey(keyphrase);
        }

        private void GenerateKey(string keyphrase)
        {
            int numtimes = 0;
            int len = keyphrase.Length;
            for (int i = 0; i < len; i++)
            {
                numtimes += (keyphrase[i]);
            }
            numtimes = numtimes % len;
            numtimes += len;
            byte[] inByte = Encoding.UTF8.GetBytes(keyphrase);
            byte[] xor = Encoding.UTF8.GetBytes(keyphrase);
            xor = ShiftBytes(xor);
            Sha256Digest digest = new Sha256Digest();
            byte[] tempkey = new byte[digest.GetDigestSize()];
            digest.Reset();
            digest.BlockUpdate(_key, 0, _key.Length);
            for (int i = 0; i < numtimes; i++)
            {
                digest.BlockUpdate(xor, 0, xor.Length);
                xor = ShiftBytes(xor);
            }

            digest.BlockUpdate(inByte, 0, inByte.Length);
            digest.DoFinal(tempkey, 0);
            _key = tempkey;
        }

        private byte[] ShiftBytes(byte[] xor)
        {
            byte[] tmp = new byte[xor.Length];
            Array.Copy(xor, 1, tmp, 0, xor.Length - 1);
            tmp[tmp.Length - 1] = xor[0];
            return tmp;
        }

        public byte[] GetKey()
        {
            return _key;
        }
    }
}
