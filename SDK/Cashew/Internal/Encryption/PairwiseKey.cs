using System.Text;
using Org.BouncyCastle.Crypto.Digests;
using System.Diagnostics;

namespace Cashew.Internal.Encryption
{
    internal class PairwiseKey : IKey
    {
        private byte[] _key = {0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12};

        internal PairwiseKey(string from, string subject, bool hasCC)
        {
            try
            {
                Sha256Digest digest = new Sha256Digest();
                byte[] inByte = Encoding.UTF8.GetBytes(from);
                byte[] h1 = new byte[digest.GetDigestSize()];
                digest.Reset();
                digest.BlockUpdate(_key, 0, _key.Length);
                digest.BlockUpdate(inByte, 0, inByte.Length);
                digest.DoFinal(h1, 0);

                inByte = Encoding.UTF8.GetBytes(subject);
                byte[] h2 = new byte[digest.GetDigestSize()];
                digest.Reset();
                digest.BlockUpdate(_key, 0, _key.Length);
                digest.BlockUpdate(inByte, 0, inByte.Length);
                digest.DoFinal(h2, 0);

                string hs3 = subject + from;
                if (hasCC)
                {
                    hs3 = from + subject;
                }
                inByte = Encoding.UTF8.GetBytes(hs3);
                byte[] h3 = new byte[digest.GetDigestSize()];
                digest.Reset();
                digest.BlockUpdate(_key, 0, _key.Length);
                digest.BlockUpdate(inByte, 0, inByte.Length);
                digest.DoFinal(h3, 0);

                GenerateKey(h1, h2, h3);
            } catch (EncoderFallbackException e)
            {
                Debug.WriteLine(e.StackTrace);
            }
        }

        private void GenerateKey(byte[] h1, byte[] h2, byte[] h3)
        {
            Sha256Digest digest = new Sha256Digest();
            byte[] h = new byte[digest.GetDigestSize()];
            digest.Reset();
            digest.BlockUpdate(_key, 0, _key.Length);
            digest.BlockUpdate(h1, 0, h1.Length);
            digest.BlockUpdate(h2, 0, h2.Length);
            digest.BlockUpdate(h3, 0, h3.Length);
            digest.DoFinal(h, 0);
            string k = System.Convert.ToBase64String(h);

            MessageKey mk = new MessageKey(k);
            _key = mk.GetKey();
        }

        public byte[] GetKey()
        {
            return _key;
        }
    }
}
