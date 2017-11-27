package com.loment.cashewnut.enc;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

public class KeyPairHelper {
	private byte[] _privateKeyBA;
	private byte[] _publicKeyBA;
	private AsymmetricCipherKeyPair _keyPair;

	public KeyPairHelper(AsymmetricCipherKeyPair pair) {
		_keyPair = pair;
		_privateKeyBA = ECCrypto.privateKeyArrayFromACKeyPair(pair);
		_publicKeyBA = ECCrypto.publicKeyArrayFromACKeyPair(pair);
	}

	public KeyPairHelper(byte[] pubKey, byte[] priKey) {
		_privateKeyBA = priKey;
		_publicKeyBA = pubKey;
		_keyPair = ECCrypto.ACKeyPairFromByteArrays(priKey, pubKey);
	}

	public AsymmetricCipherKeyPair getKeyPair() {
		return _keyPair;
	}

	public byte[] getPrivateKeyBA() {
		return _privateKeyBA;
	}

	public byte[] getPublicKeyBA() {
		return _publicKeyBA;
	}

}
