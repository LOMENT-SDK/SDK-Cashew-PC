package com.loment.cashewnut.enc;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

import org.bouncycastle.crypto.digests.SHA256Digest;

public class PairwiseKey implements Key {
	private byte[] key = new byte[] { 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12 };

	public PairwiseKey(Hashtable<?, ?> h) {
		try {

				String hs1 = (String) h.get(1);
				String hs2 = (String) h.get(2);
				Boolean hb = (Boolean) h.get(3);

				SHA256Digest digest = new SHA256Digest();
				byte[] inb = hs1.getBytes("UTF-8");
				byte[] h1 = new byte[digest.getDigestSize()];
				digest.reset();
				digest.update(key, 0, key.length);
				digest.update(inb, 0, inb.length);
				digest.doFinal(h1, 0);

				inb = null;
				inb = hs2.getBytes("UTF-8");
				byte[] h2 = new byte[digest.getDigestSize()];
				digest.reset();
				digest.update(key, 0, key.length);
				digest.update(inb, 0, inb.length);
				digest.doFinal(h2, 0);

				inb = null;
				String hs3 = hs2 + hs1;
				if (hb.booleanValue()) {
					hs3 = hs1 + hs2;
				}
				inb = hs3.getBytes("UTF-8");
				byte[] h3 = new byte[digest.getDigestSize()];
				digest.reset();
				digest.update(key, 0, key.length);
				digest.update(inb, 0, inb.length);
				digest.doFinal(h3, 0);

				generateKey(h1, h2, h3);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void generateKey(byte[] h1, byte[] h2, byte[] h3) {
		SHA256Digest digest = new SHA256Digest();
		byte[] h = new byte[digest.getDigestSize()];
		digest.reset();
		digest.update(key, 0, key.length);
		digest.update(h1, 0, h1.length);
		digest.update(h2, 0, h2.length);
		digest.update(h3, 0, h3.length);
		digest.doFinal(h, 0);
		byte[] k = Base64.encode(h).getBytes();

		MessageKey mk = new MessageKey(new String(k), true);
		key = mk.getKey();
	}

	private void generateKey(Vector<String> dig) {
		try {
			SHA256Digest digest = new SHA256Digest();
			byte[] h = new byte[digest.getDigestSize()];
			digest.reset();
			digest.update(key, 0, key.length);

			for (int i = 0; i < dig.size(); i++) {
				byte[] h1 = (dig.get(i)).getBytes("UTF-8");
				digest.update(h1, 0, h1.length);
			}

			digest.doFinal(h, 0);
			byte[] k = Base64.encode(h).getBytes();

			MessageKey mk = new MessageKey(new String(k), true);
			key = mk.getKey();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public byte[] getKey() {
		return key;
	}
}
