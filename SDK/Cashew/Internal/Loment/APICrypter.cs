using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Digests;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities.Encoders;
using System.Text;

namespace Cashew.Internal.Loment
{
    internal static class APICrypter
    {
        internal static string Encrypt(string text, string key)
        {
            var derivedKey = GenerateDerivedKey(key);

            KeyParameter keyParameter = new KeyParameter(Encoding.UTF8.GetBytes(derivedKey));
            IBufferedCipher cipher = CipherUtilities.GetCipher("AES/ECB/PKCS5Padding");
            cipher.Init(true, keyParameter);

            byte[] encryptedData = cipher.DoFinal(Encoding.UTF8.GetBytes(text));
            var encryptedText = EncodeToBase64(encryptedData);

            return encryptedText;
        }

        internal static string Decrypt(string encryptedText, string key)
        {
            var derivedKey = GenerateDerivedKey(key);

            KeyParameter keyParameter = new KeyParameter(Encoding.UTF8.GetBytes(derivedKey));
            IBufferedCipher cipher = CipherUtilities.GetCipher("AES/ECB/PKCS5Padding");
            cipher.Init(false, keyParameter);

            byte[] decryptedData = cipher.DoFinal(Base64.Decode(encryptedText));
            var decryptedText = Encoding.UTF8.GetString(decryptedData, 0, decryptedData.Length);

            return decryptedText;
        }

        internal static string GenerateDerivedKey(string input)
        {
            string reversedKey = "";

            var key = EncodeToBase64(MakeSha256Hash(MakeMD5(EncodeToBase64(MakeSha256Hash(MakeMD5(input))))));

            while (key.Length < 16)
            {
                key += key;
            }

            for (int i = key.Length - 1; i >= 0; i--)
            {
                reversedKey = reversedKey + key[i];
            }

            var derivedKey = reversedKey.Substring(0, 16);

            return derivedKey;
        }

        internal static string MakeMD5(string input)
        {
            byte[] inputData = Encoding.UTF8.GetBytes(input);

            MD5Digest md5 = new MD5Digest();
            md5.BlockUpdate(inputData, 0, inputData.Length);
            byte[] outputData = new byte[md5.GetDigestSize()];
            md5.DoFinal(outputData, 0);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < outputData.Length; i++)
            {
                int j = outputData[i] & 0xff;
                stringBuilder.Append(j.ToString("x2"));
            }

            return stringBuilder.ToString();
        }

        internal static byte[] MakeSha256Hash(string input)
        {
            var inputData = Encoding.UTF8.GetBytes(input);

            Sha256Digest digest = new Sha256Digest();
            digest.BlockUpdate(inputData, 0, inputData.Length);
            var hash = new byte[digest.GetDigestSize()];
            digest.DoFinal(hash, 0);

            return hash;
        }

        internal static string EncodeToBase64(byte[] input)
        {
            return Base64.ToBase64String(input);
        }
    }

#if DEBUG
    /// <summary>
    /// Class used to provide access to APICrypter class for Unit testing
    /// </summary>
    public static class APICrypterTestAccess
    {
        public static string Encrypt(string text, string key)
        {
            return APICrypter.Encrypt(text, key);
        }

        public static string Decrypt(string encryptedText, string key)
        {
            return APICrypter.Decrypt(encryptedText, key);
        }

        public static string GenerateDerivedKey(string input)
        {
            return APICrypter.GenerateDerivedKey(input);
        }

        public static string MakeMD5(string input)
        {
            return APICrypter.MakeMD5(input);
        }

        public static byte[] MakeSha256Hash(string input)
        {
            return APICrypter.MakeSha256Hash(input);
        }

        public static string EncodeToBase64(byte[] input)
        {
            return APICrypter.EncodeToBase64(input);
        }
    }
#endif

}
