package com.loment.cashewnut.activity.list;

import com.loment.cashewnut.crypto.CashewnutMessageCrypter;
import com.loment.cashewnut.database.BodyStore;
import com.loment.cashewnut.database.mappers.DBRecipientMapper;
import com.loment.cashewnut.enc.Key;
import com.loment.cashewnut.model.HeaderModel;
import java.util.Vector;

/**
*
* @author sekhar
*/
public class ConversationPrimer implements Comparable<Object> {

    private int messageCount;
    private HeaderModel headerModel;
    private long startDate = 0;
    private String folderType;
    private int unReadCount = 0;
    private int totalCount = 0;
    private boolean isMarked = false;
    private boolean isAck;
    String finalString = "";
    boolean restructed;
    private float tfrProgressPercentage =-1;
    int action = -1;
    public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	// private Bitmap photo = null;
    //
    // public Bitmap getPhoto() {
    // return this.photo;
    // }
    //
    // public void setPhoto(Bitmap photo) {
    // this.photo = photo;
    // }
    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public ConversationPrimer() {
    }

    /**
     *
     * @param latestMessage
     */
    public ConversationPrimer(HeaderModel latestMessage) {
        this.headerModel = latestMessage;
    }

    /**
     *
     * @return messageCount
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     *
     * @param messageCount
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     *
     * @return latestMessage
     */
    public HeaderModel getLatestMessage() {
        return headerModel;
    }

    /**
     *
     * @param latestMessage
     */
    public void setLatestMessage(HeaderModel latestMessage) {
        this.headerModel = latestMessage;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        ConversationPrimer primer = (ConversationPrimer) o;
        return headerModel.compareTo(primer.headerModel);
    }

    public String getBoxtype() {
        return folderType;
    }

    public void setBoxtype(String boxtype) {
        this.folderType = boxtype;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    public boolean isAck() {
        return isAck;
    }

    public void setAck(boolean isAck) {
        this.isAck = isAck;
    }

    public boolean isRestructed() {
        return restructed;
    }

    public void setRestructed(boolean restructed) {
        this.restructed = restructed;
    }

    public synchronized String getDisplayString() {
        //        if(finalString != null && !finalString.equals("")){
        //             return finalString + "     ";
        //        }

        DBRecipientMapper recipientMapper = DBRecipientMapper.getInstance();
        Vector recipientList = recipientMapper
                .getReceipientsModelByHeaderId(headerModel.getLocalHeaderId());
        if (headerModel.getSubject() == null || headerModel.getSubject().equalsIgnoreCase("no subject")
                || headerModel.getSubject().trim().equals("")) {
            String body = (new BodyStore()).getBody(headerModel.getLocalHeaderId());
            try {
                Key key;
                if (isGroupMessage(headerModel)) {
                    key = CashewnutMessageCrypter.getMessageKey(
                            headerModel.getHeaderVersion(),
                            headerModel.getMessageFrom(), headerModel.getSubject(),
                            null, headerModel.getGroupId().trim());
                } else {
                    key = CashewnutMessageCrypter.getMessageKey(
                            headerModel.getHeaderVersion(),
                            headerModel.getMessageFrom(), headerModel.getSubject(),
                            recipientList, "");
                }
                if (key != null) {
                    finalString = new CashewnutMessageCrypter().decryptFromBase64(
                            body, key);
                } else {
                    finalString = body + "";
                }
            } catch (Exception e) {
                finalString = body;
            }
        }
        return finalString + "     ";
    }

    public boolean isGroupMessage(HeaderModel header) {
        return header.getGroupId() != null && !header.getGroupId().equals("");
    }
    public float getTfrProgressPercentage() {
		return tfrProgressPercentage;
	}

	public void setTfrProgressPersentage(float tfrProgressPercentage) {
		this.tfrProgressPercentage = tfrProgressPercentage;
	}
    

}
