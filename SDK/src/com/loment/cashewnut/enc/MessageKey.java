package com.loment.cashewnut.enc;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * class to generate key for encrypting messages
 * @author Madhur
 *
 */
public class MessageKey implements Key {

	private byte[] key=new byte[]{0,9,8,7,6,5,4,3,2,1,1,2,3,4,5,6,7,8,9,0,0,9,8,7,6,5,4,3,2,1,12};
	
	/**
	 * old constructor kept for backward compatibility.
	 * @param keyphrase
	 */
	public MessageKey(String keyphrase){
		generateKey(keyphrase);
	}
	
	/**
	 * new constructor for better key generation
	 * @param keyphrase
	 * @param useNewAlgorithm
	 */
	public MessageKey(String keyphrase, boolean useNewAlgorithm){
		generateKeyNew(keyphrase);
	}
	
	/**
	 * old method for key generation, kept only for backward compatibility.
	 * generates key for encryption
	 * @param keyphrase
	 */
	private void generateKey(String keyphrase){
		//generate key from passphrase, currently just using digest.
		int numtimes=0;
		int len =keyphrase.length();
		for(int i=0; i< len;i++){
			numtimes+=(keyphrase.charAt(i));
		}
		numtimes = numtimes%len;
		numtimes += len;
		byte[] in = keyphrase.getBytes();
		byte[] xor = keyphrase.getBytes();
		xor = shiftBytes(xor);
		for(int i=0;i<numtimes;i++){
			for(int j=0;j<len;j++){
				in[j]=(byte) (in[j]^xor[j]);
			}
			xor = shiftBytes(xor);
		}
		Digest digest = new SHA256Digest();
		byte[] tempkey=new byte[digest.getDigestSize()];
		digest.reset();
		digest.update(key,0,key.length);
		digest.update(in,0,in.length);
		digest.doFinal(tempkey, 0);
		key=tempkey;
	}
	
	/**
	 * new algorithm for better key generation
	 * removed the xor stuff
	 * @param keyphrase
	 */
	private void generateKeyNew(String keyphrase){
		//generate key from passphrase, currently just using digest.
		int numtimes=0;
		int len =keyphrase.length();
		for(int i=0; i< len;i++){
			numtimes+=(keyphrase.charAt(i));
		}
		numtimes = numtimes%len;
		numtimes += len;
		byte[] in = keyphrase.getBytes();
		byte[] xor = keyphrase.getBytes();
		xor = shiftBytes(xor);
		Digest digest = new SHA256Digest();
		byte[] tempkey=new byte[digest.getDigestSize()];
		digest.reset();
		digest.update(key,0,key.length);
		for(int i=0;i<numtimes;i++){
			digest.update(xor,0,xor.length);
			xor = shiftBytes(xor);
		}
		
		digest.update(in,0,in.length);
		digest.doFinal(tempkey, 0);
		key=tempkey;
	}
	
	private byte[] shiftBytes(byte[] xor) {
		// TODO Auto-generated method stub
		byte[] tmp = new byte[xor.length];
		System.arraycopy(xor, 1, tmp, 0, xor.length-1);
		tmp[tmp.length-1]=xor[0];
		return tmp;
	}

	public byte[] getKey() {
		// TODO Auto-generated method stub
		return key;
	}

}
