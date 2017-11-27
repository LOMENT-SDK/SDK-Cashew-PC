package javax.microedition.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HttpConnection extends Connection{

	String GET = "GET";
	int HTTP_OK = 200;
	String POST = "POST";

	InputStream openInputStream() throws IOException;

	int getResponseCode() throws IOException;

	int getLength() throws IOException;

	void close() throws IOException;

	void setRequestMethod(String requestMethod);

	void setRequestProperty(String string, String string2);

	OutputStream openOutputStream();

	String getRequestMethod();

	String getFile();

	String getHeaderField(String string);

}
