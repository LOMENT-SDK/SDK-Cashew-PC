package com.loment.cashewnut.activity.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.bouncycastle.crypto.digests.MD5Digest;

public class ThreadIdGenerator implements Comparator<String>, Serializable {

	private static final long serialVersionUID = 8464361289407836078L;

	public int compareRight(String a, String b) {
		int bias = 0;
		int ia = 0;
		int ib = 0;

		// The longest run of digits wins. That aside, the greatest
		// value wins, but we can't know that it will until we've scanned
		// both numbers to know that they have the same magnitude, so we
		// remember it in BIAS.
		for (;; ia++, ib++) {
			char ca = charAt(a, ia);
			char cb = charAt(b, ib);

			if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
				return bias;
			} else if (!Character.isDigit(ca)) {
				return -1;
			} else if (!Character.isDigit(cb)) {
				return +1;
			} else if (ca < cb) {
				if (bias == 0) {
					bias = -1;
				}
			} else if (ca > cb) {
				if (bias == 0)
					bias = +1;
			} else if (ca == 0 && cb == 0)
				return bias;
		}
	}

	public int compare(String aIn, String bIn) {
		String a = aIn.toLowerCase();
		String b = bIn.toLowerCase();
		int ia = 0, ib = 0;
		int nza = 0, nzb = 0;
		char ca, cb;
		int result;

		while (true) {
			// only count the number of zeroes leading the last number compared
			nza = nzb = 0;

			ca = charAt(a, ia);
			cb = charAt(b, ib);

			// skip over leading spaces or zeros
			while (Character.isSpaceChar(ca) || ca == '0') {
				if (ca == '0') {
					nza++;
				} else {
					// only count consecutive zeroes
					nza = 0;
				}

				ca = charAt(a, ++ia);
			}

			while (Character.isSpaceChar(cb) || cb == '0') {
				if (cb == '0') {
					nzb++;
				} else {
					// only count consecutive zeroes
					nzb = 0;
				}

				cb = charAt(b, ++ib);
			}

			// process run of digits
			if (Character.isDigit(ca) && Character.isDigit(cb)) {
				if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0) {
					return result;
				}
			}

			if (ca == 0 && cb == 0) {
				// The strings compare the same. Perhaps the caller
				// will want to call strcmp to break the tie.
				return nza - nzb;
			}

			if (ca < cb)
				return -1;
			if (ca > cb)
				return +1;

			++ia;
			++ib;
		}
	}

	public static char charAt(String s, int i) {
		if (i >= s.length()) {
			return 0;
		} else {
			return s.charAt(i);
		}
	}

	public static List<String> sortJSONArray(List<String> to) {
		try {
			final ThreadIdGenerator comparator = new ThreadIdGenerator();

			Collections.sort(to, new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					return comparator.compare(lhs, rhs);
				}
			});
			return new Vector<String>(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return to;
	}

	public static String getMD5Value(String hash) {
		MD5Digest md5 = new MD5Digest();
		byte[] in = hash.getBytes();
		md5.update(in, 0, in.length);
		byte[] out = new byte[md5.getDigestSize()];
		md5.doFinal(out, 0);
		StringBuffer buffer = new StringBuffer(out.length * 3);
		for (int i = 0; i < out.length; i++) {
			int j = out[i] & 0xff;
			if (j < 16)
				buffer.append("0");
			buffer.append(Integer.toHexString(j));
		}
		return buffer.toString();
	}

	public static String getThreadId(String from, Vector<String> to) {
		String id = "";
		try {
			List<String> ids = new ArrayList<String>();
			for (int i = 0; i < to.size(); i++) {
				ids.add(to.get(i));
			}
			ids.add(from);
			List<String> hash = ThreadIdGenerator.sortJSONArray(ids);
			id = getMD5Value(hash.toString());
			// System.out.println("Recepients: " + ids.toString() + " - "
			// + "Thread Id: " + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}
