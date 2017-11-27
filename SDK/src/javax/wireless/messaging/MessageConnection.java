package javax.wireless.messaging;

import java.io.IOException;
import java.io.InterruptedIOException;

import javax.microedition.io.Connection;


public interface MessageConnection extends Connection{

	String TEXT_MESSAGE = "text";

	Message newMessage(String textMessage);

	void send(TextMessage binary)throws IOException,InterruptedIOException;

	void setMessageListener(MessageListener listener);

	Message receive() throws InterruptedException;

	void close();

}
