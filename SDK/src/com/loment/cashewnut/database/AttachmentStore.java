package com.loment.cashewnut.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.loment.cashewnut.crypto.CashewnutMessageCrypter;
import com.loment.cashewnut.database.mappers.DBAttachmentMapper;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.util.Helper;

public class AttachmentStore {

    public synchronized void saveAttachments(MessageModel messageModel, int localHeaderId) {
        try {
            Vector attachmentModelVector = messageModel.getAttachment();
            if (attachmentModelVector.size() > 0) {
                DBAttachmentMapper attachmentMapper = DBAttachmentMapper
                        .getInstance();
                HeaderModel headerModel = messageModel.getHeaderModel();

                Key key = null;
                if (headerModel.getGroupId() != null
                        && !headerModel.getGroupId().equals("")) {
                    key = CashewnutMessageCrypter.getMessageKey(headerModel
                            .getHeaderVersion(), headerModel.getMessageFrom(),
                            headerModel.getSubject(), null, headerModel
                            .getGroupId().trim());
                } else {
                    key = CashewnutMessageCrypter.getMessageKey(
                            headerModel.getHeaderVersion(),
                            headerModel.getMessageFrom(),
                            headerModel.getSubject(),
                            messageModel.getReceipient(), "");

                }

                for (int i = 0; i < attachmentModelVector.size(); i++) {
                    AttachmentModel attachmentModel = (AttachmentModel) attachmentModelVector
                            .elementAt(i);
                    attachmentModel.setLocalHeaderId(localHeaderId);

                    try {
                        // if file already exists
                        String absolutePath = Helper
                                .getCashewnutFolderPath("attachment");
                        String destFilePath = absolutePath + localHeaderId
                                + "_" + i + ".txt";
                        File file = new File(destFilePath);
                        if (file != null && file.exists()) {
                            file.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (attachmentModel.getAttachmentFilePath() != null
                            && !attachmentModel.getAttachmentFilePath().trim()
                            .equals("")) {
                        //for compose
                        attachmentModel
                                .setAttachmentFilePath(saveAttachmentToLocalFolderPath(
                                                localHeaderId + "_" + i,
                                                attachmentModel.getAttachmentFilePath(),
                                                key));
                    }

                    attachmentMapper.insert(attachmentModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveAttachmentToLocalFolderPath(String destinationFilename,
            String sourceFileUrl, Key pk) {

        FileInputStream in = null;
        FileOutputStream out = null;

        String absolutePath = Helper.getCashewnutFolderPath("attachment");
        String destFilePath = absolutePath + destinationFilename + ".txt";
        CashewnutMessageCrypter crypter = new CashewnutMessageCrypter();

        try {
            in = new FileInputStream(new File(sourceFileUrl));
            out = new FileOutputStream(new File(destFilePath));
            crypter.encryptAttachment(in, out, pk);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destFilePath;
    }

    public String decryptAttachmentAndSaveLocalFolderPath(
            String destinationFilename, String sourceFileName, Key pk,
            int padding) {

        FileInputStream in = null;
        FileOutputStream out = null;

        String absolutePath = Helper.getCashewnutFolderPath("attachment");
        String sourceFileUrl = absolutePath + sourceFileName + ".txt";
        String destFilePath = absolutePath + sourceFileName + "_"
                + destinationFilename;

        CashewnutMessageCrypter crypter = new CashewnutMessageCrypter();
        try {
            File destFile = new File(destFilePath);
            if (destFile != null && destFile.exists()) {
                // destFile.delete();
                return destFilePath;
            }
            File file = new File(sourceFileUrl);
            if (file != null && file.exists()) {
                in = new FileInputStream(file);
                out = new FileOutputStream(destFilePath, false);
                crypter.decryptAttachment(in, out, pk, padding);
                return destFilePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Vector getAttachmentsByHeaderLocalId(int i) {
        try {
            DBAttachmentMapper attachmentMapper = DBAttachmentMapper
                    .getInstance();
            
            Vector attachmentModelIdVector = attachmentMapper
                    .getAttachmentIdsByHeaderId(i);
            return attachmentModelIdVector;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttachmentModel getAttachmentByAttachmentLocalId(int i) {
        try {
            DBAttachmentMapper attachmentMapper = DBAttachmentMapper
                    .getInstance();
            AttachmentModel attachmentModelIdVector = attachmentMapper
                    .getAttachmentByAttachmentId(i);
            return attachmentModelIdVector;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAttachmentById(AttachmentModel attachmentModel, int id) {
        DBAttachmentMapper attachmentMapper = DBAttachmentMapper.getInstance();
        attachmentMapper.update(attachmentModel, id);
    }

    public void deleteAttachmentsByHeaderId(HeaderModel headerModel) {
        try {
            deleteAttachmentFilesByHeaderId(headerModel);

            DBAttachmentMapper header = DBAttachmentMapper.getInstance();
            header.deleteAttachmentsByHeaderId(headerModel.getLocalHeaderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAttachmentFilesByHeaderId(HeaderModel headerModel) {
        try {
            Vector localAttachmentIds = new AttachmentStore()
                    .getAttachmentsByHeaderLocalId(headerModel
                            .getLocalHeaderId());
            if (localAttachmentIds != null) {
                for (int i = 0; i < localAttachmentIds.size(); i++) {
                    int attachmentLocalID = Integer.parseInt(localAttachmentIds
                            .elementAt(i) + "");
                    AttachmentModel attachmentModel = getAttachmentByAttachmentLocalId(attachmentLocalID);
                    if (attachmentModel != null
                            && attachmentModel.getAttachmentFilePath() != null
                            && !attachmentModel.getAttachmentFilePath().trim().equalsIgnoreCase("")) {
                        File file = new File(
                                attachmentModel.getAttachmentFilePath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
