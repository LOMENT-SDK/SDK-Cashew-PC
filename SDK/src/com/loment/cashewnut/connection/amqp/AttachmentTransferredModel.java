package com.loment.cashewnut.connection.amqp;

public class AttachmentTransferredModel {

	float tfrProgressPercentage;
	int action = -1;

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	float tempUploadedSize;
	long totalSize;
	int localHeaderId;

	public float getTfrProgressPercentage() {
		return tfrProgressPercentage;
	}

	public void setTfrProgressPercentage(float f) {
		this.tfrProgressPercentage = f;
	}

	public int getLocalHeaderId() {
		return localHeaderId;
	}

	public void setLocalHeaderId(int serverMessageId) {
		this.localHeaderId = serverMessageId;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public float getTempUploadedSize() {
		return tempUploadedSize;
	}

	public void setTempUploadedSize(float percent) {
		this.tempUploadedSize = percent;
	}
}
