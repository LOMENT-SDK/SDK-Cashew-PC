package com.loment.cashewnut.enc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import com.loment.cashewnut.MyException;

/**
 * @author Madhur Utility class to encrypt decrypt data using AES. Usage: From
 *         any of the datastore classes use this code: byte[] encdata =
 *         Crypter.encrypt(new RecordKey(),data);
 * 
 *         For Message encryption use: String passphrase =
 *         target.substring(target.length()-4,4) +
 *         phoneNumber.substring(phoneNumber.length()-4,4); byte[] encdata =
 *         Crypter.encrypt(new MessageKey(passphrase),message.getBytes());
 * 
 */
public class Crypter {
	private static CBCBlockCipher encryptEngine = new CBCBlockCipher(
			new AESFastEngine());
	private static CBCBlockCipher decryptEngine = new CBCBlockCipher(
			new AESFastEngine());

	private static byte[] initVector = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,
			9, 11, 12, 13, 14, 15, 16 };

	/**
	 * @param key
	 *            Any of the key generating implementation classes
	 * @param data
	 *            Data to decrypt
	 * @return decrypted data
	 */
	public synchronized static byte[] decrypt(Key key, byte[] data) {

		try {
			ParametersWithIV param = new ParametersWithIV(new KeyParameter(
					key.getKey()), initVector);
			byte[] clear = new byte[data.length];

			decryptEngine.init(false, param);
			int blocksize = decryptEngine.getBlockSize();

			for (int i = 0; i < data.length; i += blocksize) {
				decryptEngine.processBlock(data, i, clear, i);
			}
			decryptEngine.reset();
			ByteArrayInputStream inStream = new ByteArrayInputStream(clear);
			DataInputStream dataStream = new DataInputStream(inStream);

			try {
				int size = dataStream.readShort();
				// added extra check for data which is larger then max size of
				// short.
				// this was causing few messages with large content not to load.
				if (size < 0 || size > clear.length
						|| size < (clear.length - (blocksize + 1))) {
					size = clear.length - 2;
				}
				byte[] out = new byte[size];
				dataStream.read(out);
				dataStream.close();
				return out;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new byte[] { (byte) 0xff };
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[] { (byte) 0xff };
		}
	}

	/**
	 * @param key
	 *            Any of the key generating implementation classes
	 * @param data
	 *            Data to encrypt
	 * @return encrypted data
	 */
	public synchronized static byte[] encrypt(Key key, byte[] data) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		ParametersWithIV param = new ParametersWithIV(new KeyParameter(
				key.getKey()), initVector);

		encryptEngine.init(true, param);
		int blocksize = encryptEngine.getBlockSize();
		try {
			dataStream.writeShort(data.length);
			dataStream.write(data);
			dataStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		byte[] in = new byte[((byteStream.size() + blocksize - 1) / blocksize)
				* blocksize];
		System.arraycopy(byteStream.toByteArray(), 0, in, 0, byteStream.size());
		byte[] out = new byte[in.length];

		for (int i = 0; i < in.length; i += blocksize) {
			encryptEngine.processBlock(in, i, out, i);
		}
		encryptEngine.reset();
		return out;
	}

	/**
	 * @param key
	 *            Any of the key generating implementation classes
	 * @param data
	 *            Data to decrypt
	 * @return decrypted data
	 * @throws MyException 
	 */
	public synchronized static byte[] decryptNew(Key key, byte[] data, 
			boolean hasPadding) throws MyException {

		try {
			ParametersWithIV param = new ParametersWithIV(new KeyParameter(
					key.getKey()), initVector);
			byte[] clear = new byte[data.length];

			decryptEngine.init(false, param);
			int blocksize = decryptEngine.getBlockSize();

			for (int i = 0; i < data.length; i += blocksize) {
				decryptEngine.processBlock(data, i, clear, i);
			}
			decryptEngine.reset();
			ByteArrayInputStream inStream = new ByteArrayInputStream(clear);
			// DataInputStream dataStream = new DataInputStream(inStream);

			try {
				if (hasPadding) {
					int paddingSize = 0;
					for (int i = clear.length - 1; i >= clear.length
							- blocksize; i--) {
						if (clear[i] == 0)
							paddingSize++;
						else
							break;
					}

					byte[] out = new byte[clear.length - paddingSize];
					inStream.read(out);
					return out;
				} else {
					return clear;
				}
			} catch (IOException e) {
				try {
					throw e;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		}
		return new byte[] { (byte) 0xff };
	}

	/**
	 * @param key
	 *            Any of the key generating implementation classes
	 * @param data
	 *            Data to encrypt
	 * @return encrypted data
	 */
	public synchronized static byte[] encryptNew(Key key, byte[] data) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ParametersWithIV param = new ParametersWithIV(new KeyParameter(
				key.getKey()), initVector);

		encryptEngine.init(true, param);
		int blocksize = encryptEngine.getBlockSize();
		try {
			byteStream.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		int blocks = data.length / blocksize;
		if ((data.length % blocksize) > 0) {
			blocks++;
		}
		byte[] in = new byte[blocksize * blocks];
		// byte[] in = new byte[((byteStream.size() + blocksize - 1) /
		// blocksize) * blocksize];
		System.arraycopy(byteStream.toByteArray(), 0, in, 0, byteStream.size());
		byte[] out = new byte[in.length];

		for (int i = 0; i < in.length; i += blocksize) {
			encryptEngine.processBlock(in, i, out, i);
		}
		encryptEngine.reset();
		return out;
	}
}
