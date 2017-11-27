package com.loment.cashewnut.model;

import java.io.Serializable;
import java.util.Date;

public class ContactsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2217410823710978874L;
	/**
	 * 
	 */
	public int contactId;
	public int contactServerId;
	public String contactLinkedId;
	public String firstName = "";
    public String lastName = "";
	public String walnutId = "";
	public String cashewnutId = "";
	public String peanutId = "";
	public String emailId = "";
	public String phone = "";
	public String photoUrl = "";
	public int photoExist;
	public String notes="";
	public String lastUpdateDate="";
	public String type = "";
	public int memberType = 0;
	public int status=0;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean ischeckedflag = false;

	public ContactsModel() {
		super();
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public int getContactServerId() {
		return contactServerId;
	}

	public void setContactServerId(int contactServerId) {
		this.contactServerId = contactServerId;
	}

	public String getContactLinkedId() {
		return contactLinkedId;
	}

	public void setContactLinkedId(String contactLinkedId) {
		this.contactLinkedId = contactLinkedId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}
        
        	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public String getWalnutId() {
		return walnutId;
	}

	public void setWalnutId(String walnutId) {
		this.walnutId = walnutId;
	}

	public String getCashewnutId() {
		return cashewnutId;
	}

	public void setCashewnutId(String cashewnutId) {
		this.cashewnutId = cashewnutId;
	}

	public String getPeanutId() {
		return peanutId;
	}

	public void setPeanutId(String peanutId) {
		this.peanutId = peanutId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public int getPhotoExist() {
		return photoExist;
	}

	public void setPhotoExist(int photoExist) {
		this.photoExist = photoExist;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public void setChecked(boolean ischeckedflag) {
		this.ischeckedflag = ischeckedflag;
	}

}
