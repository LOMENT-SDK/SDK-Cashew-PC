using Org.BouncyCastle.Utilities.Encoders;
using System.Text;

namespace Cashew.Internal.Encryption
{
    internal class EncryptionKey
    {
        /// <summary>
        /// Generate an encryption key, using from, subject, and the presence of CC
        /// </summary>
        /// <param name="from"></param>
        /// <param name="subject"></param>
        /// <param name="hasCcRecipients"></param>
        /// <returns></returns>
        internal static byte[] GenerateEncryptionKey(string @from, string subject = "", bool hasCcRecipients = false)
        {
            var fromHash = Sha256Crypter.GetHash(@from);
            var subjectHash = Sha256Crypter.GetHash(subject);

            // depending on the presence of a CC list, flip the concat order
            // for the final hash part
            var comboString = hasCcRecipients
                ? string.Format("{0}{1}", @from, subject)
                : string.Format("{0}{1}", subject, @from);

            // combine from, subject, and combo
            var comboHash = Sha256Crypter.GetHash(comboString);

            // put all in the same block
            var finalHash = Sha256Crypter.GetCombinedHash(fromHash, subjectHash, comboHash);

            var tempKey = Base64.Encode(finalHash);

            return Sha256Crypter.GenerateKey(Encoding.UTF8.GetString(tempKey, 0, tempKey.Length));
        }
    }
}
