package com.loment.cashewnut.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import com.google.common.io.ByteStreams;
import com.loment.cashewnut.MyException;
import com.loment.cashewnut.enc.Crypter;
import com.loment.cashewnut.enc.EncoderDecoder;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.enc.PairwiseKey;
import com.loment.cashewnut.model.RecipientModel;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CashewnutMessageCrypter {

    private static final String CASHEWNUT_CONTENT_PREFIX = "//C:";
    public static final int ATTACHMENT_CHUNK_SIZE = 2736;

    public static boolean isEncrypted(String content) {
        return content.startsWith(CASHEWNUT_CONTENT_PREFIX);
    }

    public String encryptContent(String content, Key key) {
        byte[] data = content.getBytes();
        return encryptToBase64(data, key);
    }

    private String encryptToBase64(byte[] data, Key key) {
        return EncoderDecoder.encode(encryptMessageContent(data, key));
    }

    public static byte[] encryptMessageContent(byte[] data, Key key) {
        return Crypter.encryptNew(key, data);
    }

    public String decryptFromBase64(String base64, Key key) throws MyException {
        try {
            byte[] decryptFromBase64 = EncoderDecoder.decode(base64);
            if (decryptFromBase64 != null) {
                try {
                    return new String(decryptMessageContent(
                            EncoderDecoder.decode(base64), key),"UTF8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(CashewnutMessageCrypter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (MyException e) {
            // System.out.println(e.getMessage());
        }
        return base64;
    }

    public static byte[] decryptMessageContent(byte[] data, Key key)
            throws MyException {
        try {
            return Crypter.decryptNew(key, data, true);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    public static byte[] decryptMessageContent(byte[] data, Key key,
            boolean isPadding) throws MyException {
        return Crypter.decryptNew(key, data, isPadding);
    }

    public void encryptAttachment(InputStream in, OutputStream out, Key pk) {
        try {
            for (;;) {
                InputStream chunk = ByteStreams
                        .limit(in, ATTACHMENT_CHUNK_SIZE);
                ByteArrayOutputStream chunkToEncrypt = new ByteArrayOutputStream(
                        ATTACHMENT_CHUNK_SIZE);
                if (ByteStreams.copy(chunk, chunkToEncrypt) == 0) {
                    break;
                }
                byte[] encrypted = encryptMessageContent(
                        chunkToEncrypt.toByteArray(), pk);
                out.write(EncoderDecoder.encode(encrypted).getBytes());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Assumes the input is already base64 decoded.
     *
     * @throws MyException
     */
    public void decryptAttachment(InputStream in, OutputStream out, Key key,
            int paddingsize) throws IOException, MyException {
        int blocksize = 3648;// base64 size of 2736
        int size = in.available();
        int bytesDecoded = 0;
        for (;;) {
            InputStream chunk = ByteStreams.limit(in, blocksize);
            ByteArrayOutputStream chunkToEncrypt = new ByteArrayOutputStream(
                    blocksize);
            if (ByteStreams.copy(chunk, chunkToEncrypt) == 0) {
                break;
            }
            bytesDecoded += chunkToEncrypt.size();
            if (bytesDecoded != size) {
                byte[] decrypted = decryptMessageContent(
                        EncoderDecoder.decode(chunkToEncrypt.toString()), key,
                        false);
                out.write(decrypted);
            } else {
                if (paddingsize > 0) {
                    byte[] decrypted = decryptMessageContent(
                            EncoderDecoder.decode(chunkToEncrypt.toString()),
                            key, false);
                    // if available remove that number of bytes.
                    int val = decrypted.length - paddingsize;
                    byte[] lastpart = new byte[val];
                    System.arraycopy(decrypted, 0, lastpart, 0, lastpart.length);
                    out.write(lastpart);
                } else {
                    // else remove all zero bytes from last 16 bytes.
                    byte[] decrypted = decryptMessageContent(
                            EncoderDecoder.decode(chunkToEncrypt.toString()),
                            key, true);
                    out.write(decrypted);
                }
            }
        }
        out.flush();
    }

    public synchronized static Key getMessageKey(String version, String from,
            String subject, Vector<RecipientModel> recepientsModel, String groupId) {
        Key pk = null;
        try {
            Hashtable<Integer, Comparable> pkparams = new Hashtable<Integer, Comparable>();
            if (version == null || version.equals("")) {
                version = "V0";
            }

            if (version.equals("V2") || version.equals("V1")) {
                StringBuffer toString = new StringBuffer();
                boolean isGroup = false;

                if (groupId == null || groupId.trim().equals("")) {
                    if (recepientsModel.size() > 1) {
                        isGroup = true;
                        Vector<String> recepients = new Vector<String>();
                        for (int i = 0; i < recepientsModel.size(); i++) {
                            String targetNumber = recepientsModel.get(i)
                                    .getRecepientCashewnutId();

                            recepients.add(targetNumber.substring(
                                    targetNumber.length() - 4,
                                    targetNumber.length()));
                        }
                        Collections.sort(recepients);
                        for (int i = 0; i < recepients.size(); i++) {
                            toString.append(recepients.get(i));
                        }
                    } else {
                        if (recepientsModel.isEmpty()) {
                            return null;
                        }
                        String targetNumber = recepientsModel.get(0)
                                .getRecepientCashewnutId();
                        toString.append(targetNumber);
                    }
                } else {
                    toString.append(groupId);
                    isGroup = true;
                }
                pkparams.put(1, from);
                pkparams.put(2, toString.toString());
                pkparams.put(3, Boolean.valueOf(isGroup));
                pk = new PairwiseKey(pkparams);
            }

            if (version.equals("V0")) {
                pkparams.put(1, "testuser@loment.net");
                pkparams.put(2, "test message");
                pkparams.put(3, Boolean.valueOf(false));
                pk = new PairwiseKey(pkparams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pk;
    }
}
