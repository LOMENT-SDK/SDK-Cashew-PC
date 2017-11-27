package com.loment.cashewnut.enc;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * @author Madhur
 *
 */
public class RecordKey implements Key {

	private byte[] key=new byte[]{0,9,8,7,6,5,4,3,2,1,1,2,3,4,5,6,7,8,9,0,0,9,8,7,6,5,4,3,2,1,12};
	
	public RecordKey(){
		generateKey();
	}
	private void generateKey(){
		//do something to generate the key.
		//IMEI wont work due to platform dependency
		Digest digest = new SHA256Digest();
		byte[] tempkey=new byte[digest.getDigestSize()];
		digest.reset();
		digest.update(key,0,key.length);
		digest.doFinal(tempkey, 0);
		key=tempkey;
	}
	public byte[] getKey() {
		// TODO Auto-generated method stub
		return key;
	}

}
