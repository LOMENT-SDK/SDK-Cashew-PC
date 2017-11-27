package com.loment.cashewnut.crypto;


public final class AttachmentName {
  public final String name;
  public final String type;
  public final long size;

  public AttachmentName(String name) {
    this.name = name;
    this.type = null;
    this.size = 0;
  }

  public AttachmentName(String name, String type, long size) {
    this.name = name;
    this.type = type;
    this.size= size;
  }
}
