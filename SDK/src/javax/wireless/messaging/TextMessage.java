package javax.wireless.messaging;

public interface TextMessage extends Message{

	void setAddress(String address);

	void setPayloadText(String message);

	String getPayloadText();

}
