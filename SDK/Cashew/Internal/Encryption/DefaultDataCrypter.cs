using Cashew.Services;
using Org.BouncyCastle.Utilities.Encoders;
using System;
using System.Diagnostics;
using System.IO;
using System.Text;

namespace Cashew.Internal.Encryption
{
    internal class DefaultDataCrypter : IDataCrypter
    {
        private const int ATTACHMENT_CHUNK_SIZE = 2736;

        public byte[] DecryptData(byte[] data, string sender, string recipient)
        {
            return DecryptData(data, KeyGeneration.GetMessageKey(KeyGeneration.V1, sender, recipient), true);
        }

        public byte[] DecryptGroupData(byte[] data, string sender, string groupId)
        {
            return DecryptData(data, KeyGeneration.GetMessageKey(sender, groupId), true);
        }

        public void DecryptGroupStream(Stream inStream, Stream outStream, string sender, string groupId, long inSize, long outSize)
        {
            DecryptStream(inStream, outStream, KeyGeneration.GetMessageKey(sender, groupId), inSize, outSize);
        }

        public void DecryptStream(Stream inStream, Stream outStream, string sender, string recipient, long inSize, long outSize)
        {
            DecryptStream(inStream, outStream, KeyGeneration.GetMessageKey(KeyGeneration.V1, sender, recipient), inSize, outSize);
        }

        public byte[] EncryptData(byte[] data, string sender, string recipient)
        {
            return EncryptData(data, KeyGeneration.GetMessageKey(KeyGeneration.V1, sender, recipient));
        }

        public byte[] EncryptGroupData(byte[] data, string sender, string groupId)
        {
            return EncryptData(data, KeyGeneration.GetMessageKey(sender, groupId));
        }

        public void EncryptGroupStream(Stream inStream, Stream outStream, string sender, string groupId)
        {
            EncryptStream(inStream, outStream, KeyGeneration.GetMessageKey(sender, groupId));
        }

        public void EncryptStream(Stream inStream, Stream outStream, string sender, string recipient)
        {
            EncryptStream(inStream, outStream, KeyGeneration.GetMessageKey(KeyGeneration.V1, sender, recipient));
        }

        private byte[] EncryptData(byte[] data, IKey key)
        {
            byte[] encrypted = Crypter.EncryptNew(key, data);

            if (encrypted != null)
            {
                return Encoding.UTF8.GetBytes(Convert.ToBase64String(encrypted));
            }

            return null;
        }

        private byte[] DecryptData(byte[] data, IKey key, Boolean hasPadding)
        {
            if (data == null || data.Length == 0)
            {
                return new byte[0];
            }

            try
            {
                byte[] decoded = Convert.FromBase64String(Encoding.UTF8.GetString(data, 0, data.Length));

                if (decoded != null)
                {
                    return Crypter.DecryptNew(key, decoded, hasPadding);
                } else
                {
                    throw new CryptoException("Invalid input");
                }
            } catch (DecoderFallbackException e) {
                throw new CryptoException(e.Message);
            }
        }

        private void EncryptStream(Stream inStream, Stream outStream, IKey key)
        {
            byte[] buffer = new byte[ATTACHMENT_CHUNK_SIZE];

            int bytesRead;

            try
            {
                    while ((bytesRead = inStream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    byte[] encryptedUnencoded;

                    //if we didn't read a full chunk's worth of data, we need to copy the buffer into a smaller array so empty bytes don't get encrypted
                    if (bytesRead < ATTACHMENT_CHUNK_SIZE) 
                    {
                        byte[] temp = new byte[bytesRead];
                        Array.Copy(buffer, temp, bytesRead);

                        encryptedUnencoded = Crypter.EncryptNew(key, temp);                        
                    } else
                    {
                        //buffer is full, encrypt all the bytes
                        encryptedUnencoded = Crypter.EncryptNew(key, buffer);
                    }
                    byte[] encrypted = Encoding.UTF8.GetBytes(Convert.ToBase64String(encryptedUnencoded));
                    if (encrypted != null)
                    {
                        outStream.Write(encrypted, 0, encrypted.Length);
                    }
                }
            } catch (IOException e)
            {
                Debug.WriteLine(e.StackTrace);
            } finally
            {
                outStream.Flush();
            }
        }

        private void DecryptStream(Stream inStream, Stream outStream, IKey key, long inSize, long outSize)
        {
            int blocksize = 3648; //base64 size of 2736
            int bytesDecoded = 0;
            int paddingSize = (int)(16 - (outSize % 16));
            if (paddingSize == 16)
            {
                paddingSize = 0;
            }

            byte[] buffer = new byte[blocksize];

            int bytesRead;
            try
            {
                while ((bytesRead = inStream.Read(buffer, 0, buffer.Length)) > 0)
                {
      
                    bytesDecoded += bytesRead;

                    if (bytesDecoded != inSize)
                    {
                        byte[] decrypted = Crypter.DecryptNew(key, Base64.Decode(Encoding.UTF8.GetString(buffer, 0, buffer.Length)), false);
                        outStream.Write(decrypted, 0, decrypted.Length);
                    }
                    else
                    {
                        if (paddingSize > 0)
                        {
                            byte[] decrypted = Crypter.DecryptNew(key, Base64.Decode(Encoding.UTF8.GetString(buffer, 0, bytesRead)), false);
                            outStream.Write(decrypted, 0, decrypted.Length - paddingSize);
                        }
                        else
                        {
                            //remove all zero bytes from the last 16 bytes
                            byte[] decrypted = Crypter.DecryptNew(key, Base64.Decode(Encoding.UTF8.GetString(buffer, 0, bytesRead)), true);
                            outStream.Write(decrypted, 0, decrypted.Length);
                        }
                    }

                    buffer = new byte[blocksize];
                }
            } catch (Exception e)
            {
                throw new CryptoException(e.Message);
            } finally
            {
                outStream.Flush();
            }
        }

    }
}
