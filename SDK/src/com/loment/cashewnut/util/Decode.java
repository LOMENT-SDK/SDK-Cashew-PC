package com.loment.cashewnut.util;

import java.io.*;


/**
 * Class responsible for converting different character sets and encodings
 * (Base641, QuotedPrintable).
 * 
 */
public class Decode {
	/** Flag signals if we want to print debug prints */
	private static final boolean DEBUG = false;



	/**
	 * Encodes an input string into base64 format. It means, that only 64
	 * characters are needed to encode the text string.
	 * 
	 * @author <a href="http://izhuk.com">Igor Zhukovsky</a>
	 * @param input
	 *            string to convert to base64
	 * @param isFile
	 *            set to true if input is raw file data, false if input is text
	 *            to be convert to utf-8
	 */
	public static String toBase64(String input, boolean isFile) {
		if (DEBUG) {
			System.out.println("DEBUG Decode.toBase64(input=\"" + input
					+ "\", isFile=" + isFile + ")");
		}
		byte inData[] = null;
		if (isFile) {
			inData = new byte[input.length()];
			for (int l = 0; l < input.length(); l++) {
				inData[l] = (byte) input.charAt(l);
			}
		} else {
			try {
				// Not file, standard stirng
				inData = input.getBytes("utf-8");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
				// if exception is created converting to utf-8 (meaning it is
				// likely
				// not available, use naive text conversion before converting to
				// base64
				for (int l = 0; l < input.length(); l++) {
					inData[l] = (byte) input.charAt(l);
				}
			}
		}

		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		char output[] = new char[4];
		int state = 1;
		int restbits = 0;
		int chunks = 0;

		StringBuffer encoded = new StringBuffer();

		for (int i = 0; i < inData.length; i++) {
			int ic = inData[i] & 0xFF;
			switch (state) {
			case 1:
				output[0] = base64.charAt(ic >>> 2);
				restbits = ic & 0x03;
				break;
			case 2:
				output[1] = base64.charAt((restbits << 4) | (ic >>> 4));
				restbits = ic & 0x0F;
				break;
			case 3:
				output[2] = base64.charAt((restbits << 2) | (ic >>> 6));
				output[3] = base64.charAt(ic & 0x3F);
				encoded.append(output);

				// keep no more the 76 character per line
				chunks++;
				if ((chunks % 19) == 0) {
					encoded.append("\r\n");
				}
				break;
			}
			state = (state < 3 ? state + 1 : 1);
		} // for

		/* final */
		switch (state) {
		case 2:
			output[1] = base64.charAt((restbits << 4));
			output[2] = output[3] = '=';
			encoded.append(output);
			break;
		case 3:
			output[2] = base64.charAt((restbits << 2));
			output[3] = '=';
			encoded.append(output);
			break;
		}

		if (DEBUG) {
			System.out.println("DEBUG Decode.toBase64 result=\"" + encoded
					+ "\"");
		}
		return encoded.toString();
	}

	public static String toBase64(byte[] inData, boolean isFile) {

		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		char output[] = new char[4];
		int state = 1;
		int restbits = 0;
		int chunks = 0;

		StringBuffer encoded = new StringBuffer();

		for (int i = 0; i < inData.length; i++) {
			int ic = inData[i] & 0xFF;
			switch (state) {
			case 1:
				output[0] = base64.charAt(ic >>> 2);
				restbits = ic & 0x03;
				break;
			case 2:
				output[1] = base64.charAt((restbits << 4) | (ic >>> 4));
				restbits = ic & 0x0F;
				break;
			case 3:
				output[2] = base64.charAt((restbits << 2) | (ic >>> 6));
				output[3] = base64.charAt(ic & 0x3F);
				encoded.append(output);

				// keep no more the 76 character per line
				chunks++;
				if ((chunks % 19) == 0) {
					encoded.append("\r\n");
				}
				break;
			}
			state = (state < 3 ? state + 1 : 1);
		} // for

		/* final */
		switch (state) {
		case 2:
			output[1] = base64.charAt((restbits << 4));
			output[2] = output[3] = '=';
			encoded.append(output);
			break;
		case 3:
			output[2] = base64.charAt((restbits << 2));
			output[3] = '=';
			encoded.append(output);
			break;
		}

		if (DEBUG) {
			System.out.println("DEBUG Decode.toBase64 result=\"" + encoded
					+ "\"");
		}
		return encoded.toString();
	}

	/**
	 * Does a base64 decoding for a single character.
	 * 
	 * @return 6bit number reprezented by this char if base64 encoded
	 *         <p>
	 *         Contains code from Stefan Haustein's KObjects library
	 *         (www.kobjects.org) used by permission.
	 */
	private static int decode(char c) throws Exception {
		// System.out.println("char= " + c);
		if (c >= 'A' && c <= 'Z') {
			return ((int) c) - 65;
		} else if (c >= 'a' && c <= 'z') {
			return ((int) c) - 97 + 26;
		} else if (c >= '0' && c <= '9') {
			return ((int) c) - 48 + 26 + 26;
		} else {
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new IOException();
			}
		}
	}

	/**
	 * Decodes a <code>String</code> that is encoded in the Base641 encoding. If
	 * there occures an illegel Base641 character, then a
	 * <code>MyException</code> with a propriete message is thrown.
	 */
	public static ByteArrayOutputStream decodeBase64(String s)
			throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int i = 0;
		int len = s.length(), b;

		while (true) {
			while (i < len && s.charAt(i) <= ' ') {
				i++;
			}

			if (i + 3 >= len) {
				break;
			}
			int tri = (decode(s.charAt(i)) << 18)
					+ (decode(s.charAt(i + 1)) << 12)
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));

			for (int j = 16; j >= 0; j -= 8) {
				if (j == 8 && s.charAt(i + 2) == '=') {
					break;
				}
				if (j == 0 && s.charAt(i + 3) == '=') {
					break;
				}
				b = (tri >> j) & 255;
				bos.write((char) b);
			}
			i += 4;
		}
		return bos;
	}
}
