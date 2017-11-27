package com.loment.cashewnut.model;

import com.loment.cashewnut.AppConfiguration;

public class MessageControlParameters {

    public static final int PRIORITY_INDICATOR_RED = 0;
    public static final int PRIORITY_INDICATOR_YELLOW = 1;
    public static final int PRIORITY_INDICATOR_GREEN = 2;
    public static final int PRIORITY_INDICATOR_GRAY = 3;
    public static final int PRIORITY_INDICATOR_WHITE = 4;

    public static final int ACK_SENDER = 1;
    public static final int ACK_RECP = 0;
    public static final int ACK_READ_RECEIPT = 1;
    public static final int RESTRICTED = 3;

    public static final int READ_STATUS = 1;
    public static final String ACK_MESSAGE = AppConfiguration.appConfString.message_acknowledgement;
    public static final String EXPIRY_MESSAGE = AppConfiguration.appConfString.message_expiry;
//            = "Acknowledgement is expected by sender."
//            + " You will not be able to view the message until"
//            + " you click this message and send acknowledgement. ";

    private String priority = "";
    private String expiry = "";
    private String restricted = "";
    private String messageAck = "";

    private long messageSchedule = -1;
    private int years;
    private int months;
    private int date;
    private int hours;
    private int minutes;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getRestricted() {
        return restricted;
    }

    public void setRestricted(String restricted) {
        this.restricted = restricted;
    }

    public String getMessageAck() {
        return messageAck;
    }

    public void setMessageAck(String messageAck) {
        this.messageAck = messageAck;
    }

    public long getMessageSchedule() {
        return messageSchedule;
    }

    public void setMessageSchedule(long messageSchedule) {
        this.messageSchedule = messageSchedule;
    }

}
