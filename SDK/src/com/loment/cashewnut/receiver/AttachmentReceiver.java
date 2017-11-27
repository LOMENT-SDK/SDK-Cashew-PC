package com.loment.cashewnut.receiver;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.loment.cashewnut.CashewnutActivity;
import com.loment.cashewnut.connection.amqp.AttachmentDataConnection;
import com.loment.cashewnut.crypto.CashewnutMessageCrypter;
import com.loment.cashewnut.database.AttachmentStore;
import com.loment.cashewnut.database.HeaderStore;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.jsonReceiverModel.BodyPartsJson;
import com.loment.cashewnut.model.AttachmentModel;
import com.loment.cashewnut.model.HeaderModel;
import com.loment.cashewnut.model.MessageModel;
import com.loment.cashewnut.util.Helper;

import javafx.scene.control.ProgressBar;

public class AttachmentReceiver implements ReceiveServerRespListener {

	private HashMap<String, String> tokenMapper = new HashMap<String, String>();
	public static String RECEIVED_ATTACHMENT_TOKEN_AMQP = "cashew_attachment";

	public AttachmentReceiver() {
		try {
			Receiver.getInstance().addReceiverListener(this);
		} catch (Exception ex) {
		}
	}

	public String getMessage(int id) {
		BodyPartsJson header = new BodyPartsJson();
		String token = "MSG_ATT-" + Receiver.getInstance().getToken();
		String data = header.getAttachmentByIdJsonAmqp(id);
		Receiver.getInstance().receiveAmqp(data, token);
		tokenMapper.put(token, id + "");
		return token;
	}

	public void listenForResponse(String token, String response) {
		try {
			JSONObject jsonMessageObject = new JSONObject(response);
			if (jsonMessageObject.has("file_name")) {
				saveAttachmentAmqp(token, jsonMessageObject);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private synchronized void saveAttachmentAmqp(String token,
			JSONObject jsonMessageObject) {
		try {
			if (jsonMessageObject.has("file_name")) {

				String downloadedFileName = jsonMessageObject
						.getString("file_name");

				int id = Integer.parseInt("" + tokenMapper.get(token));
				HeaderModel messageModel = new HeaderStore().getHeaderById(id,
						false);
				if (messageModel != null) {
					String tempFilePath = getTempFilePath(token);
					if (!AttachmentDataConnection.getInstance()
							.downloadAttachment(downloadedFileName,
									tempFilePath)) {
						// System.out.println("Download error: " +
						// tempFilePath);
						return;
					}

					String absolutePath = Helper
							.getCashewnutFolderPath("attachment");
					String destFilePath = absolutePath
							+ messageModel.getLocalHeaderId() + "_" + 0
							+ ".txt";

					File file = new File(tempFilePath);
					File reFile = new File(destFilePath);
					if (file.renameTo(reFile)) {
						AttachmentStore attachmentStore = new AttachmentStore();
						Vector attachmentModelVector = attachmentStore
								.getAttachmentsByHeaderLocalId(messageModel
										.getLocalHeaderId());
						if (attachmentModelVector.size() > 0) {
							int localid = (Integer) attachmentModelVector
									.elementAt(0);
							AttachmentModel attachmentModel = attachmentStore
									.getAttachmentByAttachmentLocalId(localid);

							attachmentModel.setAttachmentFilePath(destFilePath);
							attachmentStore.updateAttachmentById(
									attachmentModel,
									attachmentModel.getLocalAttachmentId());
							decryptAttachment(messageModel, file);
							addToList(messageModel);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTempFilePath(String token) {
		String absolutePath = Helper.getCashewnutFolderPath("attachment");
		String destFilePath = absolutePath + token + ".txt";
		return destFilePath;
	}

	private String decryptAttachment(HeaderModel headerModel, File reFile) {
		try {
			if (reFile.exists()) {
				DBRecipientMapper recipientMapper = DBRecipientMapper
						.getInstance();
				Vector recipientList = recipientMapper
						.getReceipientsModelByHeaderId(headerModel
								.getLocalHeaderId());

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
							headerModel.getSubject(), recipientList, "");

				}
				AttachmentStore attachmentStore = new AttachmentStore();
				Vector attachmentModelVector = attachmentStore
						.getAttachmentsByHeaderLocalId(headerModel
								.getLocalHeaderId());
				for (int i = 0; i < attachmentModelVector.size();) {
					int localid = (Integer) attachmentModelVector.elementAt(i);
					AttachmentModel attachmentModel = attachmentStore
							.getAttachmentByAttachmentLocalId(localid);

					String name = attachmentModel.getAttachmentName();
					int padding = attachmentModel.getPadding();

					String sourcefileName = headerModel.getLocalHeaderId()
							+ "_" + i;
					String destPath = attachmentStore
							.decryptAttachmentAndSaveLocalFolderPath(name,
									sourcefileName, key, padding);
					return destPath;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addToList(HeaderModel headerModel) {
		// try {
		// fire to list
		// ((CashewnutActivity) CashewnutActivity.currentActivity)
		// .processMessage(headerModel, MessageModel.ACTION_UPDATE);

		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
}