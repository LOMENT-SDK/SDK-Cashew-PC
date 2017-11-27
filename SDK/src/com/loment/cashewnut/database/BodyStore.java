package com.loment.cashewnut.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.loment.cashewnut.crypto.CashewnutMessageCrypter;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.model.BodyModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.util.Helper;

public class BodyStore {

    public synchronized void saveBody(MessageModel messageModel, int localHeaderId,
            boolean isFromReceiver) {

        HeaderModel headerModel = messageModel.getHeaderModel();
        BodyModel bodyModel = messageModel.getBodyModel();

        String absolutePath = Helper.getCashewnutFolderPath("body");
        String destFilePath = absolutePath + localHeaderId + ".txt";

        FileOutputStream fos = null;
        try {
            File file = new File(destFilePath);
            if (file.exists()) {
                file.delete();
            }

            fos = new FileOutputStream(file, true);
            if (bodyModel != null && bodyModel.getBodyContent() != null
                    && bodyModel.getBodyContent().trim().length() > 0) {
                if (!isFromReceiver) {
                    CashewnutMessageCrypter crypter = new CashewnutMessageCrypter();
                    Key key = null;
                    if (headerModel.getGroupId() != null && !headerModel.getGroupId().equals("")) {
                        key = CashewnutMessageCrypter.getMessageKey(
                                headerModel.getHeaderVersion(), headerModel.getMessageFrom(),
                                headerModel.getSubject(), null, headerModel.getGroupId().trim());
                    } else {
                        key = CashewnutMessageCrypter.getMessageKey(
                                headerModel.getHeaderVersion(), headerModel.getMessageFrom(),
                                headerModel.getSubject(), messageModel.getReceipient(), "");

                    }
                    fos.write(crypter.encryptContent(
                            bodyModel.getBodyContent(), key).getBytes());
                } else {
                    String body = bodyModel.getBodyContent();
                    try {
                        body = Helper.replace(body, "\\", "");
                        body = Helper.replace(body, "\r", "");
                        body = Helper.replace(body, "\n", "");
                        body = Helper.replace(body, "\\r", "");
                        body = Helper.replace(body, "\\n", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fos.write(body.getBytes());
                }
            } else {
                CashewnutMessageCrypter crypter = new CashewnutMessageCrypter();
                Key key = null;
                if (headerModel.getGroupId() != null && !headerModel.getGroupId().equals("")) {
                    key = CashewnutMessageCrypter.getMessageKey(
                            headerModel.getHeaderVersion(), headerModel.getMessageFrom(),
                            headerModel.getSubject(), null, headerModel.getGroupId().trim());
                } else {
                    key = CashewnutMessageCrypter.getMessageKey(
                            headerModel.getHeaderVersion(), headerModel.getMessageFrom(),
                            headerModel.getSubject(), messageModel.getReceipient(), "");

                }
                fos.write(crypter.encryptContent("   ", key).getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBody(int id) {
        FileInputStream in = null;
        String absolutePath = Helper.getAbsolutePath("body");
        if (!absolutePath.endsWith("//")) {
            absolutePath = absolutePath + "//";
        }
        String sourceFilePath = absolutePath + id + ".txt";

        try {
            File file = new File(sourceFilePath);
            if (file.exists()) {
                in = new FileInputStream(new File(sourceFilePath));
                StringBuilder sb = new StringBuilder();
                Reader r = new InputStreamReader(in); // or whatever encoding
                char[] buf = new char[1024];
                int amt = r.read(buf);
                while (amt > 0) {
                    sb.append(buf, 0, amt);
                    amt = r.read(buf);
                }
                return sb.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void deleteBody(int localHeaderId) {
        try {
            FileInputStream in = null;
            String absolutePath = Helper.getAbsolutePath("body");
            if (!absolutePath.endsWith("//")) {
                absolutePath = absolutePath + "//";
            }
            String sourceFilePath = absolutePath + localHeaderId + ".txt";
            File file = new File(sourceFilePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
