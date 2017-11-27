/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loment.cashewnut.enc;

import java.io.UnsupportedEncodingException;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author sai
 */
public class EncoderDecoder {

	public static String encode(byte[] data) {
		try {
			return new String(Base64.encode(data),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decode(String data) {
		try {
			return Base64.decode(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
